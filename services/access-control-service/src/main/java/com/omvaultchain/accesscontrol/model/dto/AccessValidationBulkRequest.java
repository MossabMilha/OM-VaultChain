package com.omvaultchain.accesscontrol.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AccessValidationBulkRequest {
    @NotNull
    private List<String> usersIds;
    @NotNull
    private String fileId;
}
