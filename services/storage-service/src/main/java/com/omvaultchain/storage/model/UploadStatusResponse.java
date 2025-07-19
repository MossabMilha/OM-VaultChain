package com.omvaultchain.storage.model;

import lombok.Data;

import java.time.Instant;

@Data
public class UploadStatusResponse {
    private String uploadId;
    private String status;
    private String fileName;
    private long uploadedBytes;
    private long totalBytes;
    private Instant startedAt;
}
