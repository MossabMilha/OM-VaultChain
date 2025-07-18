package com.omvaultchain.storage.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadRequest {
    private String fileName;
    private String mimeType;
    private String ownerId;
    public String getEncryptedFileName(String originalFileName) {
        int lastDotIndex = originalFileName.lastIndexOf('.');
        String baseName = lastDotIndex > 0 ? originalFileName.substring(0, lastDotIndex) : originalFileName;
        return baseName + ".enc";
    }
}
