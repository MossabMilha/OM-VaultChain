

package com.omvaultchain.controller;

import com.omvaultchain.model.EncryptionRequest;
import com.omvaultchain.model.EncryptionResponse;
import com.omvaultchain.service.AsymmetricEncryptionService;
import com.omvaultchain.service.CryptoOrchestrator;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * CryptoController
 *
 * REST controller for the encryption microservice. It exposes an API endpoint
 * to perform AES file encryption and RSA-based key wrapping for secure sharing
 * among multiple recipients.
 *
 * Endpoint:
 * - POST /api/encryption/encrypt
 *
 * Workflow:
 * - Receives raw file data and recipient public keys from the client.
 * - Performs AES-256-GCM encryption on the file content.
 * - Wraps the AES key using each recipient's RSA public key.
 * - Returns the encrypted file, IV, encrypted keys, and file hash.
 *
 * Notes:
 * - Public keys must be sent in Base64 format.
 * - AES keys are never stored and only included encrypted in the response.
 * - The client is responsible for storing and decrypting the data locally.
 */

@RestController
@RequestMapping("/api/encryption") //Base URL OF THIS CLASS => http://localhost:8080/api/encryption
public class CryptoController {
    @Autowired
    private CryptoOrchestrator orchestrator;

    @Autowired
    private AsymmetricEncryptionService AE_Service;

    @PostMapping("/encrypt")
    public ResponseEntity<EncryptionResponse> encrypt(@RequestBody EncryptionRequest request)throws Exception{
        Map<String, PublicKey> publicKeys = new HashMap<>();
        for(Map.Entry<String,String> entry : request.getRecipientPublicKeys().entrySet()){
            publicKeys.put(entry.getKey(), AE_Service.loadPublicKeyFromBase64(entry.getValue()));
        }
        EncryptionResponse response = orchestrator.encryptFile(request.getData(), publicKeys);
        return ResponseEntity.ok(response);
    }

}
