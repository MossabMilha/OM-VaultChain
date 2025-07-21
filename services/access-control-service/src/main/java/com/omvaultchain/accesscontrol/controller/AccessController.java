package com.omvaultchain.accesscontrol.controller;

import com.omvaultchain.accesscontrol.model.AccessPermission;
import com.omvaultchain.accesscontrol.model.dto.*;
import com.omvaultchain.accesscontrol.service.AccessGrantService;
import com.omvaultchain.accesscontrol.service.AccessRevokeService;
import com.omvaultchain.accesscontrol.service.AccessValidationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/access")
@AllArgsConstructor
public class AccessController {
    private final AccessGrantService accessGrantService;
    private final AccessRevokeService accessRevokeService;
    private final AccessValidationService accessValidationService;
    /**
     * Grants access to a specific user for a given file.
     *
     * Request Body Example:
     * {
     *   "fileId": "file123",
     *   "userId": "user456",
     *   "encryptedKey": "abc123..."
     * }
     *
     * @return Details of the granted access.
     */
    @PostMapping("/grant")
    public ResponseEntity<AccessGrantResponse> grantAccess(@RequestBody AccessGrantRequest request) {
        AccessGrantResponse response = accessGrantService.grantAccess(request);
        return ResponseEntity.ok(response);
    }
    /**
     * Grants access to multiple users for a given file.
     *
     * Request Body Example:
     * {
     *   "fileId": "file123",
     *   "users": [
     *     { "userId": "user1", "encryptedKey": "abc..." },
     *     { "userId": "user2", "encryptedKey": "def..." }
     *   ]
     * }
     *
     * @return Summary of access granted to each user.
     */
    @PostMapping("/grant/multiple")
    public ResponseEntity<AccessGrantMultipleResponse> grantMultipleAccess(@Valid @RequestBody AccessGrantMultipleRequest request) {
        AccessGrantMultipleResponse response = accessGrantService.grantAccessMultiple(request);
        return ResponseEntity.ok(response);
    }
    /**
     * Grants temporary access to users for a file (with expiration time).
     *
     * Request Body Example:
     * {
     *   "fileId": "file123",
     *   "accessList": [
     *     {
     *       "userId": "user1",
     *       "encryptedKey": "abc...",
     *       "expiresAt": "2025-08-01T00:00:00Z"
     *     }
     *   ]
     * }
     *
     * @return Summary of temporary access grants.
     */
    @PostMapping("/grant/temporary")
    public ResponseEntity<AccessGrantMultipleResponse> grantTemporaryAccess(@Valid @RequestBody AccessGrantTemporaryRequest request){
        AccessGrantMultipleResponse response = accessGrantService.grantTemporaryAccess(request);
        return ResponseEntity.ok(response);
    }
    /**
     * Revokes access for a specific user from a file.
     *
     * Request Body Example:
     * {
     *   "fileId": "file123",
     *   "userId": "user456"
     * }
     *
     * @return Confirmation message.
     */
    @PostMapping("/revoke")
    public ResponseEntity<String> revokeAccess(@Valid @RequestBody AccessRevokeRequest request){
        accessRevokeService.revokeAccess(request);
        return ResponseEntity.ok("Access Revoked Successfully");
    }
    /**
     * Revokes access for multiple users from a file.
     *
     * Request Body Example:
     * {
     *   "fileId": "file123",
     *   "userIds": ["user1", "user2", "user3"]
     * }
     *
     * @return Confirmation message.
     */
    @PostMapping("/revoke/multiple")
    public ResponseEntity<String> revokeMultipleAccess(@Valid @RequestBody AccessRevokeMultipleRequest request){
        accessRevokeService.revokeMultipleAccess(request);
        return ResponseEntity.ok(" Access Revoked For Multiple Users Successfully");
    }
    /**
     * Removes all access permissions for a file, except for the owner.
     *
     * Request Body Example:
     * {
     *   "fileId": "file123"
     * }
     *
     * @return Confirmation message.
     */
    @PostMapping("/remove-all")
    public ResponseEntity<String> removeAllAccess(@Valid @RequestBody AccessRemoveAllRequest request) {
        accessRevokeService.removeAllAccess(request);
        return ResponseEntity.ok("All Access Removed Successfully Except For The File Owner.");
    }
    /**
     * Validates whether a user has access to a specific file.
     *
     * Request Body Example:
     * {
     *   "fileId": "file123",
     *   "userId": "user456"
     * }
     *
     * @return Object indicating access status and encrypted key if access is granted.
     */
    @PostMapping("/validate")
    public ResponseEntity<AccessValidationResponse> validateAccess(@Valid @RequestBody AccessValidationRequest request) {
        AccessValidationResponse response = accessValidationService.validateAccess(request);
        return ResponseEntity.ok(response);
    }
    /**
     * Validates access for multiple users to a given file.
     *
     * Request Body Example:
     * {
     *   "fileId": "file123",
     *   "userIds": ["user1", "user2", "user3"]
     * }
     *
     * @return Map of user IDs and their corresponding access status.
     */
    @PostMapping("/validate/bulk")
    public ResponseEntity<AccessValidationBulkResponse> validateBulkAccess(@Valid @RequestBody AccessValidationBulkRequest request) {
        AccessValidationBulkResponse response = accessValidationService.validateBulkAccess(request);
        return ResponseEntity.ok(response);
    }
    /**
     * Lightweight access check for a single user and file.
     *
     * Request Body Example:
     * {
     *   "fileId": "file123",
     *   "userId": "user456"
     * }
     *
     * @return true if user has access, false otherwise.
     */
    @GetMapping("check")
    public ResponseEntity<Boolean> checkAccess(@Valid @RequestBody AccessCheckRequest request) {
        boolean hasAccess = accessValidationService.checkAccess(request);
        return ResponseEntity.ok(hasAccess);
    }


}
