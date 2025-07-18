package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.PinataUploadResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IPFSClient {

    @Value("${pinata.api-key}")
    private String apiKey;

    @Value("${pinata.secret-api-key}")
    private String secretApiKey;

    @Value("${pinata.base-url}")
    private String gatewayUrl;

    @Value("${pinata.pinning-url:https://api.pinata.cloud/pinning/pinFileToIPFS}")
    private String pinningUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(IPFSClient.class);

    /**
     * Uploads a file from disk to IPFS (via Pinata)
     */
    public String uploadFile(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return uploadFile(inputStream, file.getName());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file " + file.getName(), e);
        }
    }

    /**
     * Uploads file to IPFS using InputStream
     */
    public String uploadFile(InputStream inputStream, String fileName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("pinata_api_key", apiKey);
            headers.set("pinata_secret_api_key", secretApiKey);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            ByteArrayResource resource = new ByteArrayResource(inputStream.readAllBytes()) {
                @Override
                public String getFilename() {
                    return fileName;
                }
            };

            body.add("file", resource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<PinataUploadResponse> response = restTemplate.postForEntity(
                    pinningUrl,
                    requestEntity,
                    PinataUploadResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                logger.info("File uploaded to Pinata successfully. CID: {}", response.getBody().getIpfsHash());
                return response.getBody().getIpfsHash();
            } else {
                logger.error("Failed to upload to Pinata. Response code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to upload to Pinata: " + response.getStatusCode());
            }

        } catch (Exception e) {
            logger.error("Error uploading file to Pinata", e);
            throw new RuntimeException("Error uploading file to Pinata", e);
        }
    }



}
