package com.omvaultchain.storage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PinataUploadResponse {
    @JsonProperty("IpfsHash")
    private String ipfsHash;

    @JsonProperty("PinSize")
    private Long pinSize;

    @JsonProperty("Timestamp")
    private String timestamp;
}
