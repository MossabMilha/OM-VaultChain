package com.omvaultchain.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadResponse {
    private String fileId;
    private String cid;
    private String fileName;
    private String status;

}
