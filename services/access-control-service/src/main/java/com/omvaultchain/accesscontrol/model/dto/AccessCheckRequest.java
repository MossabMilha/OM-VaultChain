package com.omvaultchain.accesscontrol.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessCheckRequest {
    @NotNull
    private String fileId;
    @NotNull
    private String userId;
}
