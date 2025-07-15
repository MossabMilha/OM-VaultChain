package com.omvaultchain.blockchain.controller.dto;

public class FileRegisterRequest {
    private String cid;
    private String fileHash;

    public String getCid() {return cid;}
    public String getFileHash() {return fileHash;}

    public void setCid(String cid) {this.cid = cid;}
    public void setFileHash(String fileHash) {this.fileHash = fileHash;}
}
