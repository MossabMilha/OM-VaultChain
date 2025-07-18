package com.omvaultchain.storage.model;

import lombok.Data;

import java.util.UUID;

@Data
public class UploadRequest {
    private String fileName;
    private String mimeType;
    private long size;
    private String encryptedCid;
    private String fileHash;
    private UUID ownerId;

}
