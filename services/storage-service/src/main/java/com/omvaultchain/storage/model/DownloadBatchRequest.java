package com.omvaultchain.storage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DownloadBatchRequest {
    private List<String> fileIds;

    @JsonProperty("CIDs")
    private List<String> CIDs;
}
