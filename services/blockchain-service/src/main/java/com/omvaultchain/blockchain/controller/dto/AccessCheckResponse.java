package com.omvaultchain.blockchain.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessCheckResponse {
    private String cid;
    private String walletAddress;
    private boolean hasAccess;
    private Instant grantTime;
    private String encryptedKey;
}
