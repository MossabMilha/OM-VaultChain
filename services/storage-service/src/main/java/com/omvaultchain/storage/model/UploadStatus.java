package com.omvaultchain.storage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
public class UploadStatus {
    @Id
    private String uploadId;
    private String ownerId;
    private String fileName;
    private boolean resumable;
    private boolean completed;
    private boolean canceled;
    private long uploadedBytes;
    private long totalBytes;
    private Instant startedAt;
}
