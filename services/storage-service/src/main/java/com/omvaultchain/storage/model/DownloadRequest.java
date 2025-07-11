package com.omvaultchain.storage.model;

public class DownloadRequest {
    private String cid;
    public DownloadRequest(String cid){
        this.cid = cid;
    }

    public String getCid() {return cid;}

    public void setCid(String cid) {this.cid = cid;}
}
