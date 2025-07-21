package com.omvaultchain.accesscontrol.controller;

import com.omvaultchain.accesscontrol.model.AccessPermission;
import com.omvaultchain.accesscontrol.model.dto.*;
import com.omvaultchain.accesscontrol.service.AccessGrantService;
import com.omvaultchain.accesscontrol.service.AccessRevokeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/access")
@AllArgsConstructor
public class AccessController {
    private final AccessGrantService accessGrantService;
    private final AccessRevokeService accessRevokeService;

    @PostMapping("/grant")
    public ResponseEntity<AccessGrantResponse> grantAccess(@Valid @RequestBody AccessGrantRequest request) {
        AccessGrantResponse response = accessGrantService.grantAccess(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/grant/multiple")
    public ResponseEntity<AccessGrantMultipleResponse> grantMultipleAccess(@Valid @RequestBody AccessGrantMultipleRequest request) {
        AccessGrantMultipleResponse response = accessGrantService.grantAccessMultiple(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/grant/temporary")
    public ResponseEntity<AccessGrantMultipleResponse> grantTemporaryAccess(@Valid @RequestBody AccessGrantTemporaryRequest request){
        AccessGrantMultipleResponse response = accessGrantService.grantTemporaryAccess(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/revoke")
    public ResponseEntity<String> revokeAccess(@Valid @RequestBody AccessRevokeRequest request){
        accessRevokeService.revokeAccess(request);
        return ResponseEntity.ok("Access Revoked Successfully");
    }
    @PostMapping("/revoke/multiple")
    public ResponseEntity<String> revokeMultipleAccess(@Valid @RequestBody AccessRevokeMultipleRequest request){
        accessRevokeService.revokeMultipleAccess(request);
        return ResponseEntity.ok(" Access Revoked For Multiple Users Successfully");
    }
    @PostMapping("/remove-all")
    public ResponseEntity<String> removeAllAccess(@Valid @RequestBody AccessRemoveAllRequest request) {
        accessRevokeService.removeAllAccess(request);
        return ResponseEntity.ok("All Access Removed Successfully Except For The File Owner.");
    }

}
