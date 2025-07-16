package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class VersionInfo {
    private long versionNumber;
    private String cid;
    private long timestamp;

    public VersionInfo(long versionNumber, String cid, long timestamp) {
        this.versionNumber = versionNumber;
        this.cid = cid;
        this.timestamp = timestamp;
    }
}
