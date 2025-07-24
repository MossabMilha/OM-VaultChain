package com.omvaultchain.storage.model;

import lombok.Data;

import java.util.List;
@Data
public class BatchUploadRequest {
    private String ownerId;
    private List<FileData> files;

    @Data
    public static class FileData {
        private String fileData;
        private String fileName;
        private String mimeType;
        private String iv;
        private String encryptedKey;
        private String fileHash;
        public String getEncryptedFileName(String originalFileName) {
            int lastDotIndex = originalFileName.lastIndexOf('.');
            String baseName = lastDotIndex > 0 ? originalFileName.substring(0, lastDotIndex) : originalFileName;
            return baseName + ".enc";
        }
    }
}
