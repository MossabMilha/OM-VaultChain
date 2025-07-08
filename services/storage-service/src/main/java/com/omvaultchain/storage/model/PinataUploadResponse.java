package com.omvaultchain.storage.model;
import com.fasterxml.jackson.annotation.JsonProperty;
public class PinataUploadResponse {
    @JsonProperty("IpfsHash")
    private String ipfsHash;

    @JsonProperty("PinSize")
    private long pinSize;

    @JsonProperty("Timestamp")
    private String timestamp;

    public String getIpfsHash(){return this.ipfsHash;}
    public long getPinSize(){return this.pinSize;}
    public String getTimestamp(){return this.timestamp;}

    public void setIpfsHash(String ipfsHash){this.ipfsHash = ipfsHash;}
    public void setPinSize(long pinSize) {this.pinSize = pinSize;}
    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}
}
