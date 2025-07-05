package com.omvaultchain.model;

import java.util.Map;

public class KeyEnvelope {
    private final Map<String,byte[]> encryptedKeys;

    public KeyEnvelope(Map<String,byte[]> encryptedKeys) {
        this.encryptedKeys = encryptedKeys;
    }
    public Map<String,byte[]> getEncryptedKeys(){
        return encryptedKeys;
    }
    public byte[] getEncryptedKeyForRecipient(String recipientId){
        return encryptedKeys.get(recipientId);
    }

}
