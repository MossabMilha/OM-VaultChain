package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class FileStatusRespond {
    private String fileId;
    private String status; // e.g., "active", "archived", "deleted"
    public FileStatusRespond(String fileId, String status) {
        this.fileId = fileId;
        this.status = status;
    }

}
