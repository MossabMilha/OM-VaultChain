package com.omvaultchain.accesscontrol.service;

import com.omvaultchain.accesscontrol.model.AccessPermission;
import com.omvaultchain.accesscontrol.model.dto.*;
import com.omvaultchain.accesscontrol.repository.AccessPermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccessGrantService {
    private final AccessPermissionRepository accessPermissionRepository;

    public AccessGrantResponse grantAccess(AccessGrantRequest request) {
        try{
            AccessPermission permission = new AccessPermission();
            permission.setId(UUID.randomUUID().toString());
            permission.setFileId(request.getFileId());
            permission.setUserId(request.getUserId());
            permission.setEncryptedKey(request.getEncryptedKey());
            permission.setGrantedAt(Instant.now());
            permission.setIsActive(true);
            AccessPermission saved = accessPermissionRepository.save(permission);
            return new AccessGrantResponse(saved.getId(), saved.getFileId(), saved.getUserId(), saved.getGrantedAt(), saved.getIsActive());
        } catch (Exception e) {
            throw new RuntimeException("Failed To Grant Access : " + e.getMessage());
        }
    }
    public AccessGrantMultipleResponse grantAccessMultiple(AccessGrantMultipleRequest request) {
        List<AccessGrantMultipleResponse.SingleGrantResult> results = new ArrayList<>();
        for (AccessGrantMultipleRequest.UserAccessItem userItem : request.getUsers()){
            try{
                AccessPermission permission = new AccessPermission();
                permission.setId(UUID.randomUUID().toString());
                permission.setFileId(request.getFileId());
                permission.setUserId(userItem.getUserId());
                permission.setEncryptedKey(userItem.getEncryptedKey());
                permission.setGrantedAt(Instant.now());
                permission.setIsActive(true);
                AccessPermission saved = accessPermissionRepository.save(permission);
                results.add(new AccessGrantMultipleResponse.SingleGrantResult(
                        saved.getId(),
                        saved.getUserId(),
                        saved.getGrantedAt(),
                        true,
                        "Access Granted Successfully"
                ));
            }catch (Exception e){
                results.add(new AccessGrantMultipleResponse.SingleGrantResult(
                        null,
                        userItem.getUserId(),
                        Instant.now(),
                        false,
                        "Failed To Grant Access: " + e.getMessage()
                ));

            }
        }
        return new AccessGrantMultipleResponse(
                request.getFileId(),
                (int) results.stream().filter(AccessGrantMultipleResponse.SingleGrantResult::getSuccess).count(),
                results
        );
    }

    public AccessGrantMultipleResponse grantTemporaryAccess(AccessGrantTemporaryRequest request){
        List<AccessGrantMultipleResponse.SingleGrantResult> resultsList = new ArrayList<>();
        for (AccessGrantTemporaryRequest.TemporaryAccessEntry entry : request.getEntries()){
            try{
                AccessPermission permission = new AccessPermission();
                permission.setId(UUID.randomUUID().toString());
                permission.setFileId(request.getFileId());
                permission.setUserId(entry.getUserId());
                permission.setEncryptedKey(entry.getEncryptedKey());
                permission.setGrantedAt(Instant.now());
                permission.setRevokedAt(entry.getExpiresAt());
                permission.setIsActive(true);
                AccessPermission saved = accessPermissionRepository.save(permission);
                resultsList.add(AccessGrantMultipleResponse.SingleGrantResult.builder()
                        .permissionId(permission.getId())
                        .userId(entry.getUserId())
                        .grantedAt(permission.getGrantedAt())
                        .success(true)
                        .message("Access granted successfully")
                        .build());
//                accessSchedulerService.scheduleRevoke(entry.getUserId(), request.getFileId(), entry.getExpiresAt());
            } catch (Exception e) {
                resultsList.add(AccessGrantMultipleResponse.SingleGrantResult.builder()
                        .permissionId(null)
                        .userId(entry.getUserId())
                        .grantedAt(null)
                        .success(false)
                        .message("Failed to grant access: " + e.getMessage())
                        .build());
            }
        }
        return AccessGrantMultipleResponse.builder()
                .fileId(request.getFileId())
                .grantedCount((int) resultsList.stream().filter(AccessGrantMultipleResponse.SingleGrantResult::getSuccess).count())
                .results(resultsList)
                .build();
    }
}
