package com.omvaultchain.accesscontrol.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccessGrantRequest {
    @NotBlank(message = "File ID cannot be blank")
    private String fileId;
    @NotBlank(message = "userId is required")
    private String userId;
    @NotBlank(message = "encryptedKey is required")
    private String encryptedKey;
}
