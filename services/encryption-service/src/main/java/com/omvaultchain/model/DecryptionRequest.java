package com.omvaultchain.model;

import lombok.Data;

@Data
public class DecryptionRequest {
    private byte[] encryptedData;
    private byte[] encryptedAESKey;
    private String privateKeyBase64;
    private byte[] iv;
}
