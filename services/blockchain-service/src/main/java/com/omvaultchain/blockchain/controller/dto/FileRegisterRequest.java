package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class FileRegisterRequest {
    private String uploaderWallet;
    private String cid;
    private String fileHash;
    private BigInteger version;

}
