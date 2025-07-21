package com.omvaultchain.accesscontrol.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessValidationBulkResponse {
    private Map<String,Boolean> accessMap;
}
