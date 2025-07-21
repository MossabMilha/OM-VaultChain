package com.omvaultchain.accesscontrol.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessGrantMultipleResponse {
    private String fileId;
    private int grantedCount;
    private List<SingleGrantResult > results;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SingleGrantResult{
        private String permissionId;
        private String userId;
        private Instant grantedAt;
        private Boolean success;
        private String message;
    }
}
