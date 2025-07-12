package com.omvaultchain.storage.model;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class UploadResponse {
    private String fileName;
    private String mimeType;
    private long size;
    private String cid;
    private String fileHash;
    private String ownerWallet;
    private Instant uploadAt;
    private String status;
}
