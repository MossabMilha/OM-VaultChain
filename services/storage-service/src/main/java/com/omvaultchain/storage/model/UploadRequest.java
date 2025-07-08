package com.omvaultchain.storage.model;

public class UploadRequest {
    private String fileName;
    private byte[] Data;

    public UploadRequest(){}
    public UploadRequest(String fileName,byte[] Data){
        this.fileName = fileName;
        this.Data = Data;
    }

    public String getFileName(){return this.fileName;}
    public byte[] getData() {return Data;}

    public void setFileName(String fileName){this.fileName = fileName;}

    public void setData(byte[] data) {this.Data = data;}
}
