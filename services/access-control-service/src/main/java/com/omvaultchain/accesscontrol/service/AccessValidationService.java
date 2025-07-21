package com.omvaultchain.accesscontrol.service;

import com.omvaultchain.accesscontrol.model.AccessPermission;
import com.omvaultchain.accesscontrol.model.dto.*;
import com.omvaultchain.accesscontrol.repository.AccessPermissionRepository;
import com.omvaultchain.accesscontrol.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.bouncycastle.util.Strings;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccessValidationService {
    private final AccessPermissionRepository accessPermissionRepository;
    private final FileRepository fileRepository;
    public AccessValidationResponse validateAccess(AccessValidationRequest request) {
        String fileId = request.getFileId();
        String userId = request.getUserId();

        Optional<AccessPermission> permissionOpt = accessPermissionRepository.findByFileIdAndUserIdAndIsActiveTrue(fileId, userId);

        if (permissionOpt.isPresent()) {
            return new AccessValidationResponse(true, permissionOpt.get().getEncryptedKey());
        }

        String ownerId = fileRepository.findOwnerIdByFileId(fileId).orElse(null);

        if (ownerId != null && ownerId.equals(userId)) {
            return new AccessValidationResponse(true, null); // No encrypted key stored, but still has access
        }

        return new AccessValidationResponse(false, null);
    }
    public AccessValidationBulkResponse validateBulkAccess(AccessValidationBulkRequest request){
        String fileId = request.getFileId();
        List<String> userIds = request.getUsersIds();
        String ownerId = fileRepository.findOwnerIdByFileId(fileId).orElseThrow(() -> new IllegalArgumentException("File Not Found"));
        Map<String, Boolean> accessMap = new HashMap<>();
        for (String userId : userIds) {
            if (ownerId != null && ownerId.equals(userId)) {
                accessMap.put(userId, true);
                continue;
            }
            boolean hasPermission = accessPermissionRepository.findByFileIdAndUserIdAndIsActiveTrue(fileId, userId).isPresent();
            accessMap.put(userId, hasPermission);
        }
        return new AccessValidationBulkResponse(accessMap);
    }
    public boolean checkAccess(AccessCheckRequest request) {
        String fileId = request.getFileId();
        String userId = request.getUserId();
        String ownerId = fileRepository.findOwnerIdByFileId(fileId).orElse(null);
        if (ownerId != null && ownerId.equals(userId)) {
            return true;
        }
        return accessPermissionRepository.findByFileIdAndUserIdAndIsActiveTrue(fileId, userId).isPresent();
    }
}
