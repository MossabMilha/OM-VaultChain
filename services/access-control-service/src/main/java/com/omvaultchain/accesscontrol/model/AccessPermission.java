package com.omvaultchain.accesscontrol.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "access_permission")
@Data
public class AccessPermission {
    @Id
    private String id;

    private String fileId;
    private String userId;
    private String encryptedKey;
    private Instant grantedAt;
    private Instant revokedAt;
    private Boolean isActive;
}
