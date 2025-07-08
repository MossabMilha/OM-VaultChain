package com.omvaultchain.storage.model;

public class UploadResponse {
    private String CID;
    public UploadResponse(){}
    public UploadResponse(String CID){
        this.CID = CID;
    }

    public String getCID(){return this.CID;}

    public void setCID(String CID){this.CID = CID;}

}
