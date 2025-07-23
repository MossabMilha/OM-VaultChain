package com.omvaultchain.storage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "access_permissions",uniqueConstraints = @UniqueConstraint(columnNames = {"file_id", "user_id"}))
@Data
public class AccessPermission {
    @Id
    private String Id;

    @Column(name = "file_id", nullable = false)
    private String fileId;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "encrypted_key", nullable = false)
    private String encryptedKey;
    @Column(name = "granted_at",columnDefinition = "TIMESTAMP")
    private Instant createdAt;
    @Column(name = "revoked_at", columnDefinition = "TIMESTAMP")
    private Instant revokedAt;
    @Column(name = "is_active")
    private Boolean isActive;
}
