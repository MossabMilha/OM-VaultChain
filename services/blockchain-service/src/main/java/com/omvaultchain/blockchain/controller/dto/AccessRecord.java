package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AccessRecord {
    private String walletAddress;
    private String encryptedKey;
    private Instant grantedAt;
    private boolean active;

    public AccessRecord(String walletAddress, String encryptedKey, Instant grantedAt, boolean active) {
        this.walletAddress = walletAddress;
        this.encryptedKey = encryptedKey;
        this.grantedAt = grantedAt;
        this.active = active;
    }
}
