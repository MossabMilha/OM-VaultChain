package com.omvaultchain.storage.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeUploadResponse {
    private String fileId;
    private String fileName;
    private String status;
    private long uploadedBytes;
    private long totalBytes;
    private String cid;
}
