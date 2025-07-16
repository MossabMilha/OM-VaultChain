package com.omvaultchain.blockchain.controller;

import com.omvaultchain.blockchain.contracts.FileRegistry;
import com.omvaultchain.blockchain.controller.dto.*;
import com.omvaultchain.blockchain.service.AccessRightsService;
import com.omvaultchain.blockchain.service.FileRegistryService;
import com.omvaultchain.blockchain.service.VersioningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {
    @Autowired
    private FileRegistryService fileRegistryService;
    @Autowired
    private AccessRightsService accessRightsService;
    @Autowired
    private VersioningService versioningService;

    @PostMapping("/register-file")
    public ResponseEntity<?> registerFile(@RequestBody FileRegisterRequest request ){
        String txHash = fileRegistryService.registerFileOnBlockChain(request.getCid(), request.getFileHash());
        return ResponseEntity.ok(Map.of("transactionHash", txHash));
    }
    /*
     * Access Rights Management API
     * */
    @PostMapping("/grant-access")
    public ResponseEntity<?> grantAccess(@RequestBody GrantAccessRequest request){
        try {
            String txHash = accessRightsService.grantAccess(request.getCid(),request.getGranteeWallet(), request.getEncryptedKey());
            return ResponseEntity.ok(Map.of("transactionHash", txHash));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @PostMapping("/revoke-access")
    public ResponseEntity<?> revokeAccess(@RequestBody AccessRevokeRequest request){
        accessRightsService.revokeAccess(request.getCid(),request.getWalletAddress());
        return ResponseEntity.ok(Map.of("Message", "Access Revoked Successfully"));
    }

    @GetMapping("/has-access")
    public ResponseEntity<?> hasAccess(@RequestParam String cid, @RequestParam String walletAddress){
        try {
            AccessCheckResponse response = accessRightsService.hasAccess(cid, walletAddress);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }
    /* File Versioning */
    @PostMapping("/add-version")
    public ResponseEntity<TransactionResponse> addVersion(@RequestBody AddVersionRequest request){
        String txHash = versioningService.addVersion(request.getFileId(), request.getCid());
        return ResponseEntity.ok(new TransactionResponse(txHash));
    }
    @PostMapping("/rollback-version")
    public ResponseEntity<TransactionResponse> rollbackToVersion(@RequestBody RollbackVersionRequest  request){
        String txHash = versioningService.rollbackToVersion(request.getFileId(), request.getVersionNumber());
        return ResponseEntity.ok(new TransactionResponse(txHash));
    }
    @GetMapping("/version-history")
    public ResponseEntity<List<VersionInfo>> getVersionHistory(@RequestParam String fileId) {
        return ResponseEntity.ok(versioningService.getVersionHistory(fileId));
    }




}
