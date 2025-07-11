package com.omvaultchain.storage.model;

import java.util.Map;

public class UploadRequest {

    private String fileName;
    private String mimeType;
    private long size;
    private String ownerWallet;
    private int version;

    private String encryptedData; // Base64-encoded encrypted file
    private String hashedData;    // SHA-256 hash of encrypted data
    private String iv;            // IV used in AES encryption
    private Map<String, String> encryptedKeys; // userWallet -> Encrypted AES key

    public UploadRequest() {}

    public UploadRequest(String fileName, String mimeType, long size, String ownerWallet, int version, String encryptedData, String hashedData, String iv, Map<String, String> encryptedKeys) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.size = size;
        this.ownerWallet = ownerWallet;
        this.version = version;
        this.encryptedData = encryptedData;
        this.hashedData = hashedData;
        this.iv = iv;
        this.encryptedKeys = encryptedKeys;
    }

    // Getters
    public String getFileName() {return fileName;}
    public String getMimeType() {return mimeType;}
    public long getSize() {return size;}
    public String getOwnerWallet() {return ownerWallet;}
    public int getVersion() {return version;}
    public String getEncryptedData() {return encryptedData;}
    public String getHashedData() {return hashedData;}
    public String getIv() {return iv;}
    public Map<String, String> getEncryptedKeys() {return encryptedKeys;}
    // Setters
    public void setFileName(String fileName) {this.fileName = fileName;}
    public void setMimeType(String mimeType) {this.mimeType = mimeType;}
    public void setSize(long size) {this.size = size;}
    public void setOwnerWallet(String ownerWallet) {this.ownerWallet = ownerWallet;}
    public void setVersion(int version) {this.version = version;}
    public void setEncryptedData(String encryptedData) {this.encryptedData = encryptedData;}
    public void setHashedData(String hashedData) {this.hashedData = hashedData;}
    public void setIv(String iv) {this.iv = iv;}
    public void setEncryptedKeys(Map<String, String> encryptedKeys) {this.encryptedKeys = encryptedKeys;}
}
