package com.omvaultchain.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchUploadResponse {
    private String fileName;
    private String cid;
    private String status;
    private String message;

}
