package com.omvaultchain.accesscontrol.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessRemoveAllRequest {
    @NotNull
    private String fileId;
}
