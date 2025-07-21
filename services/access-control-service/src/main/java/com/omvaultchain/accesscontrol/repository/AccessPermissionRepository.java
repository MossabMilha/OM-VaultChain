package com.omvaultchain.accesscontrol.repository;

import com.omvaultchain.accesscontrol.model.AccessPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessPermissionRepository extends JpaRepository<AccessPermission, String> {
    Optional<AccessPermission> findByFileAndUserIdAndIsActiveTrue(String fileId,String userId);
    List<AccessPermission> findAllByFileIdAndIsActiveTrue(String fileId);
}
