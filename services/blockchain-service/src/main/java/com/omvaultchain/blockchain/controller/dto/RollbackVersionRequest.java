package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class RollbackVersionRequest {
    private String fileId;
    private long versionNumber;

}
