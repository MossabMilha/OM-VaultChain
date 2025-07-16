package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class VersionAtRequest {
    private String fileId;
    private BigInteger versionNumber;
}
