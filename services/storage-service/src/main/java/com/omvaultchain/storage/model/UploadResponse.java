package com.omvaultchain.storage.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


public class UploadResponse {
    private String fileName;
    private String mimeType;
    private long size;
    private String cid;
    private String fileHash;
    private String ownerWallet;
    private Instant uploadAt;
    private String status = "COMPLETED";

    public UploadResponse() {}

    public UploadResponse(String fileName, String mimeType, long size,
                          String cid, String fileHash, String ownerWallet, Instant uploadAt) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.size = size;
        this.cid = cid;
        this.fileHash = fileHash;
        this.ownerWallet = ownerWallet;
        this.uploadAt = uploadAt;
    }

    // Getters
    public String getFileName() { return fileName; }
    public String getMimeType() { return mimeType; }
    public long getSize() { return size; }
    public String getCid() { return cid; }
    public String getFileHash() { return fileHash; }
    public String getOwnerWallet() { return ownerWallet; }
    public Instant getUploadAt() { return uploadAt; }
    public String getStatus() { return status; }

    // Setters
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public void setSize(long size) { this.size = size; }
    public void setCid(String cid) { this.cid = cid; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }
    public void setOwnerWallet(String ownerWallet) { this.ownerWallet = ownerWallet; }
    public void setUploadAt(Instant uploadAt) { this.uploadAt = uploadAt; }
    public void setStatus(String status) { this.status = status; }


}
