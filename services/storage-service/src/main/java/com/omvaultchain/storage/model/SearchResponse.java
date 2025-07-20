package com.omvaultchain.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private List<FileMetadata> results;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}
