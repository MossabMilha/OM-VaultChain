package com.omvaultchain.storage.model;

import java.time.Instant;

public class FileMetadata {
    private String fileName;
    private String mimeType;
    private long size;
    private Instant uploadAt;

    public FileMetadata(){}
    public FileMetadata(String fileName,String mimeType,long size,Instant uploadAt){
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.size = size;
        this.uploadAt = uploadAt;
    }

    public String getFileName(){return this.fileName;}
    public String getMimeType() {return mimeType;}
    public long getSize() {return size;}
    public Instant getUploadAt() {return uploadAt;}

    public void setFileName(String fileName){this.fileName = fileName;}
    public void setMimeType(String mimeType) {this.mimeType = mimeType;}
    public void setSize(long size) {this.size = size;}
    public void setUploadAt(Instant uploadAt){this.uploadAt = uploadAt;}
}
