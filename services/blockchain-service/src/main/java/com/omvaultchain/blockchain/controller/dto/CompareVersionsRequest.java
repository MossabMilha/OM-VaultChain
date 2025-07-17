package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class CompareVersionsRequest {
    private String fileId;
    private int version1;
    private int version2;
}
