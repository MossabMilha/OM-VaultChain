package com.omvaultchain.storage.model;

public class DownloadResponse {
    private String base64EncryptedData;

    public DownloadResponse(String base64EncryptedData){
        this.base64EncryptedData = base64EncryptedData;
    }
    public String getEncryptedData(){return this.base64EncryptedData;}

    public void setEncryptedData(String encryptedData) {this.base64EncryptedData = encryptedData;}


}
