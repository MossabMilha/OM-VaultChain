package com.omvaultchain.accesscontrol.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AccessGrantTemporaryRequest {
    @NotNull
    private String fileId;
    @NotEmpty
    private List<TemporaryAccessEntry> entries;

    @Data
    public static class TemporaryAccessEntry {
        @NotNull
        private String userId;
        @NotNull
        private String encryptedKey;
        @NotNull
        private Instant expiresAt;
    }

    private String grantedBy;
}
