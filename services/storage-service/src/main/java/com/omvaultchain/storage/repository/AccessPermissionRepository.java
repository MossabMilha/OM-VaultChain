package com.omvaultchain.storage.repository;

import com.omvaultchain.storage.model.AccessPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessPermissionRepository extends JpaRepository<AccessPermission, String> {
    boolean existsByFileIdAndUserId(String fileId, String userId);
}
