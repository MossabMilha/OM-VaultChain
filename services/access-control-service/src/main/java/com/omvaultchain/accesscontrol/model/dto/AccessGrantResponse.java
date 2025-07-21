package com.omvaultchain.accesscontrol.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessGrantResponse {
    private String permissionId;
    private String fileId;
    private String userId;
    private Instant grantedAt;
    private Boolean isActive;
}
