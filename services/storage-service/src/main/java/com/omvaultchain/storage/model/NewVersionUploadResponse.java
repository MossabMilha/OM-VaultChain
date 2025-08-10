package com.omvaultchain.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewVersionUploadResponse {
    private String fileName;
    private String cid;
    private String fileId;

}
