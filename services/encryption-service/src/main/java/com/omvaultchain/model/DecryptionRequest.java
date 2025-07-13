package com.omvaultchain.model;

import lombok.Data;

@Data
public class DecryptionRequest {
    private String encryptedData;
    private String encryptedAESKey;
    private String privateKeyBase64;
    private String iv;
}
