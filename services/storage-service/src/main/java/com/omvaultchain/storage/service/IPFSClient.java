package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.PinataUploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@Service
public class IPFSClient {

    @Value("${pinata.api-key}")
    String ApiKey;

    @Value("${pinata.secret-api-key}")
    String SecretApiKey;

    @Value("${pinata.base-url}")
    String gatewayUrl;

    @Value("${pinata.pinning-url:https://api.pinata.cloud/pinning/pinFileToIPFS}")
    private String pinningUrl;

    private final RestTemplate restTemplate= new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(IPFSClient.class);


    /*
    * This Methode Will Upload Data To Pinata And Return The CID
    */
    public String uploadFile(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return uploadFile(inputStream, file.getName());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file " + file.getName(), e);
        }
    }
    /**
     * Upload using InputStream + fileName
     */
    public String uploadFile(InputStream inputStream, String fileName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("pinata_api_key", ApiKey);
            headers.set("pinata_secret_api_key", SecretApiKey);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            ByteArrayResource resource = new ByteArrayResource(inputStream.readAllBytes()) {
                @Override
                public String getFilename() {
                    return fileName; // supply your file name here
                }
            };

            body.add("file", resource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<PinataUploadResponse> response =
                    restTemplate.postForEntity(pinningUrl, requestEntity, PinataUploadResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getIpfsHash();
            } else {
                throw new RuntimeException("Failed to upload to Pinata: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to Pinata", e);
        }
    }
    /**
     * Download a file from IPFS using CID
     */
    public byte[] DownloadData(String cid){
        String url = gatewayUrl + cid;
        logger.info("Downloading file from IPFS : {}",url);
        try{
            ResponseEntity<byte[]> response = restTemplate.getForEntity(url,byte[].class);
            if (response.getStatusCode()== HttpStatus.OK){
                logger.info("Successfully downloaded file with CID: {}", cid);
                return response.getBody();
            }else{
                logger.error("Failed to download from IPFS: {}, StatusCode: {}", cid, response.getStatusCode());
                throw new RuntimeException("Failed to download from IPFS : " + response.getStatusCode());
            }
        }catch (Exception e){
            logger.error("Error while downloading from IPFS for CID {}: {}", cid, e.getMessage());
            throw new RuntimeException("Download failed", e);
        }
    }

}
