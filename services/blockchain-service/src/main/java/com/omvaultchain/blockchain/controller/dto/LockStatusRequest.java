package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class LockStatusRequest {
    private String fileId;
    private int version;
}
