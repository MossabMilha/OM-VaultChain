package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class LockVersionRequest {
    private String fileId;
    private int versionNumber;
}
