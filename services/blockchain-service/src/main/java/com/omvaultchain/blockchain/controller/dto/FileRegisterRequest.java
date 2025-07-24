package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class FileRegisterRequest {
    private String ownerId;
    private String cid;
    private String fileHash;
    private long version;

}
