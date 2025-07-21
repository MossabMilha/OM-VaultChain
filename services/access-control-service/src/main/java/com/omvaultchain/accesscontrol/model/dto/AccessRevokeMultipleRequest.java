package com.omvaultchain.accesscontrol.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AccessRevokeMultipleRequest {
    @NotNull
    private String fileId;
    @NotNull
    private List<String> userIds;
}
