package com.omvaultchain.test_service_combine;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import java.util.Base64;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestOrchestratorController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/secure-upload")
    public ResponseEntity<?> secureUpload(@RequestParam("file") MultipartFile file) throws Exception {
        // 1. Generate RSA key pair dynamically
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String base64PublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String base64PrivateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());


        byte[] fileBytes = IOUtils.toByteArray(file.getInputStream());
        String base64File = Base64.getEncoder().encodeToString(fileBytes);

        // 3. Build encryption payload with generated public key
        Map<String, Object> encryptionRequest = Map.of(
                "data", base64File,
                "recipientPublicKeys", Map.of("user1", base64PublicKey)
        );


        ResponseEntity<Map> encryptionResponse = restTemplate.postForEntity(
                "http://localhost:8002/api/encryption/encrypt",

                encryptionRequest,
                Map.class
        );

        if (!encryptionResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(500).body("Encryption failed");
        }

        // 5. Decode encrypted data
        String encryptedBase64 = (String) encryptionResponse.getBody().get("encryptedData");
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);

        // 6. Wrap as file for upload
        Resource encryptedFile = new ByteArrayResource(encryptedBytes) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files", encryptedFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<?> requestEntity = new HttpEntity<>(body, headers);

        // 7. Call storage-service
        ResponseEntity<String> storageResponse = restTemplate.exchange(
                "http://localhost:8003/files/upload",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // 8. Return encryption and storage info, **plus the generated keys**
        return ResponseEntity.ok(Map.of(
                "encryption", encryptionResponse.getBody(),
                "storage", storageResponse.getBody(),
                "generatedKeys", Map.of(
                        "publicKey", base64PublicKey,
                        "privateKey", base64PrivateKey
                )
        ));
    }
}
