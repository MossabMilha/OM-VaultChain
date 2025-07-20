package com.omvaultchain.storage.model;

import lombok.Data;

import java.time.Instant;

@Data
public class SearchRequest {
    private String nameContains;
    private String mimeType;
    private Long minSize;
    private Long maxSize;
    private Instant startDate;
    private String endDate;
    private String sortBy = "createdAt";
    private String sortOrder = "desc";
    private Integer page = 0;
    private Integer size = 20;

}
