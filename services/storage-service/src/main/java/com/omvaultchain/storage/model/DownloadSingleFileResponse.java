package com.omvaultchain.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadSingleFileResponse {
    private String encryptedData;
    private String fileName;
    private String mimeType;
    private long fileSize;
    private String cid;
    private String fileHash;
    private String iv;

}
