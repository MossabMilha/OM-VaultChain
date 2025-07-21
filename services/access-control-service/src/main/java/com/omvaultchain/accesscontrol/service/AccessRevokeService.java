package com.omvaultchain.accesscontrol.service;

import com.omvaultchain.accesscontrol.model.AccessPermission;
import com.omvaultchain.accesscontrol.model.dto.*;
import com.omvaultchain.accesscontrol.repository.AccessPermissionRepository;
import com.omvaultchain.accesscontrol.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccessRevokeService {
    private final AccessPermissionRepository accessPermissionRepository;
    private final FileRepository fileRepository;

    public void revokeAccess(AccessRevokeRequest request) {
        String fileId = request.getFileId();
        String targetUserId = request.getUserId();

        String ownerId = fileRepository.findOwnerIdByFileId(fileId).orElseThrow(() -> new IllegalArgumentException("File not found"));
        if(ownerId.equals(targetUserId)){
            throw new IllegalArgumentException("Cannot Revoke Access For Owner");
        }
        AccessPermission permission = accessPermissionRepository.findByFileAndUserIdAndIsActiveTrue(fileId, targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("No Active Permission Found For User"));
        permission.setIsActive(false);
        permission.setRevokedAt(Instant.now());
        accessPermissionRepository.save(permission);

    }
    public void revokeMultipleAccess(AccessRevokeMultipleRequest request){
        String fileId = request.getFileId();
        String ownerId = fileRepository.findOwnerIdByFileId(fileId).orElseThrow(() -> new IllegalArgumentException("File Not Found"));
        for(String userId : request.getUserIds()){
            if(ownerId.equals(userId)){
                continue;
            }
            Optional<AccessPermission> permissionOpt = accessPermissionRepository.findByFileAndUserIdAndIsActiveTrue(fileId, userId);
            permissionOpt.ifPresent(permission -> {{
            permission.setIsActive(false);
            permission.setRevokedAt(Instant.now());}
            accessPermissionRepository.save(permission);
            });
        }
    }
    public void removeAllAccess(AccessRemoveAllRequest request){
        String fileId = request.getFileId();

        String ownerId = fileRepository.findOwnerIdByFileId(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));

        List<AccessPermission> permissions = accessPermissionRepository.findAllByFileIdAndIsActiveTrue(fileId);

        for (AccessPermission permission : permissions) {
            if (permission.getUserId().equals(ownerId)) {
                continue;
            }

            permission.setIsActive(false);
            permission.setRevokedAt(Instant.now());
        }

        accessPermissionRepository.saveAll(permissions);
    }
}
