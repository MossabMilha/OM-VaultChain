package com.omvaultchain.accesscontrol.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessValidationResponse {
    private Boolean hasAccess;
    private String encryptedKey;
}
