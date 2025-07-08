package com.omvaultchain.storage.service;
import com.omvaultchain.storage.model.PinataUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Map;

@Service
public class IPFSClient {

    @Value("${pinata.api-key}")
    String ApiKey;

    @Value("${pinata.secret-api-key}")
    String SecretApiKey;

    @Value("${pinata.base-url}")
    String gatewayUrl;

    private static final String PINATA_PINING_URL ="https://api.pinata.cloud/pinning/pinFileToIPFS";

    private final RestTemplate restTemplate= new RestTemplate();

    /*
    * This Methode Will Upload Data To Pinata And Return The CID
    */
    public String UploadData(MultipartFile file) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("pinata_api_key",ApiKey);
            headers.set("pinata_secret_api_key",SecretApiKey);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
            ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()){
                @Override
                public String getFilename(){
                    return file.getOriginalFilename();
                }
            };
            body.add("file",fileAsResource);
            HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<>(body,headers);
            ResponseEntity<PinataUploadResponse> response = restTemplate.postForEntity(PINATA_PINING_URL, requestEntity, PinataUploadResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getIpfsHash();
            }else {
                throw new RuntimeException("Failed To Upload To Pinata : " + response.getStatusCode());
            }

        }catch (IOException e){
            throw new RuntimeException("Error reading File ", e);
        }
    }
    public byte[] DownLoadData(String cid){
        String url = gatewayUrl + cid;
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url,byte[].class);
        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else{
            throw new RuntimeException("Failed To Download From IPFS : " + response.getStatusCode());
        }
    }
}
