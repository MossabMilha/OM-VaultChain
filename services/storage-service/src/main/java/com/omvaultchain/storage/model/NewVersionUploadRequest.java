package com.omvaultchain.storage.model;

import lombok.Data;

@Data
public class NewVersionUploadRequest {
    private String fileId;           // required for new version
    private String fileData;         // Base64 encrypted file content
    private String fileName;
    private String mimeType;
    private String iv;
    private String encryptedKey;
    private String fileHash;
    private String ownerId;

    public String getEncryptedFileName(String originalFileName) {
        int lastDotIndex = originalFileName.lastIndexOf('.');
        String baseName = lastDotIndex > 0 ? originalFileName.substring(0, lastDotIndex) : originalFileName;
        return baseName + ".enc";
    }
}

