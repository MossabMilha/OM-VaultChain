package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class GrantAccessRequest {
    private String cid;
    private String granteeWallet;
    private String encryptedKey;

}
