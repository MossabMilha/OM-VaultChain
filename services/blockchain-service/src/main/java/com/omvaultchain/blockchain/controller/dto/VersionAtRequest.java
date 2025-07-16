package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class VersionAtRequest {
    private String fileId;
    private int versionNumber;
}
