package com.omvaultchain.service;

import com.omvaultchain.model.KeyEnvelope;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
@Service
public class KeyEnvelopeBuilder {

    private final AsymmetricEncryptionService asymmetricEncryptionService;

    public KeyEnvelopeBuilder(AsymmetricEncryptionService asymmetricEncryptionService) {
        this.asymmetricEncryptionService = asymmetricEncryptionService;
    }

    public KeyEnvelope buildEnvelope(byte[] AES_Key, Map<String, PublicKey> recipientPublicKeys) throws Exception {
        Map<String, byte[]> encryptedKeys = new HashMap<>();
        for (Map.Entry<String, PublicKey> entry : recipientPublicKeys.entrySet()){
            String recipientId = entry.getKey();
            PublicKey publicKey = entry.getValue();
            byte[] encryptedKey = asymmetricEncryptionService.wrapAESKey(AES_Key,publicKey);
            encryptedKeys.put(recipientId,encryptedKey);
        }
        return new KeyEnvelope(encryptedKeys);
    }

}
