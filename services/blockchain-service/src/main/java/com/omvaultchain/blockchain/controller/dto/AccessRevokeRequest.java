package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class AccessRevokeRequest {
    private String cid;
    private String walletAddress;
}
