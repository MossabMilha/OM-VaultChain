package com.omvaultchain.blockchain.controller;

import com.omvaultchain.blockchain.controller.dto.*;
import com.omvaultchain.blockchain.service.AccessRightsService;
import com.omvaultchain.blockchain.service.FileRegistryService;
import com.omvaultchain.blockchain.service.VersioningService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Registers a file on the blockchain.
     *
     * @param request FileRegisterRequest with fields:
     *                - cid: the IPFS content identifier (String)
     *                - fileHash: the hash of the file content (String)
     * @return ResponseEntity with a map containing the blockchain transaction hash.
     *
     * Example request body:
     * {
     *   "cid": "QmXyz123abc...",
     *   "fileHash": "abcdef1234567890"
     * }
     */
    @PostMapping("/register-file")
    public ResponseEntity<?> registerFile(@RequestBody FileRegisterRequest request ){
        String txHash = fileRegistryService.registerFileOnBlockChain(request.getCid(), request.getFileHash());
        return ResponseEntity.ok(Map.of("transactionHash", txHash));
    }

    /*
     * Access Rights Management APIs
     */

    /**
     * Retrieves the list of users who have access to a given CID.
     *
     * @param cid The content identifier (String)
     * @return ResponseEntity with a list of AccessRecord objects.
     *
     * Example GET request:
     * /blockchain/access-list?cid=QmXyz123abc...
     */
    @GetMapping("/access-list")
    public ResponseEntity<List<AccessRecord>> getAccessList(@RequestParam String cid) {
        List<AccessRecord> accessList = accessRightsService.getAccessList(cid);
        return ResponseEntity.ok(accessList);
    }

    /**
     * Grants access to a single user for a specific CID.
     *
     * @param request GrantAccessRequest with fields:
     *                - cid: the content identifier (String)
     *                - granteeWallet: wallet address to grant access (String)
     *                - encryptedKey: encrypted key for secure access (String)
     * @return ResponseEntity with TransactionResponse containing the transaction hash.
     *
     * Example request body:
     * {
     *   "cid": "QmXyz123abc...",
     *   "granteeWallet": "0xAbC1234...",
     *   "encryptedKey": "encryptedKeyForUser"
     * }
     */
    @PostMapping("/grant-access")
    public ResponseEntity<TransactionResponse> grantAccess(@RequestBody GrantAccessRequest request){
        String txHash = accessRightsService.grantAccess(request.getCid(),request.getGranteeWallet(), request.getEncryptedKey());
        return ResponseEntity.ok(new TransactionResponse(txHash));
    }

    /**
     * Grants access rights to multiple users for a given CID.
     *
     * @param request GrantMultipleAccessRequest with fields:
     *                - cid: the content identifier (String)
     *                - userId: list of maps each containing:
     *                    - walletAddress: user's wallet address (String)
     *                    - encryptedKey: encrypted key for user (String)
     * @return ResponseEntity with TransactionResponse containing transaction hash.
     *
     * Example request body:
     * {
     *   "cid": "QmXyz123abc...",
     *   "userId": [
     *     {
     *       "walletAddress": "0xAbC1234...",
     *       "encryptedKey": "encryptedKeyForUser1"
     *     },
     *     {
     *       "walletAddress": "0xDeF5678...",
     *       "encryptedKey": "encryptedKeyForUser2"
     *     }
     *   ]
     * }
     */
    @PostMapping("/grant-multiple-access")
    public ResponseEntity<TransactionResponse> grantMultipleAccess(@RequestBody GrantMultipleAccessRequest request){
        String txHash = accessRightsService.grantMultipleAccess(request.getCid(), request.getUserId());
        return ResponseEntity.ok(new TransactionResponse(txHash));
    }

    /**
     * Revokes access for a specific user on a given CID.
     *
     * @param request AccessRevokeRequest with fields:
     *                - cid: the content identifier (String)
     *                - walletAddress: wallet address to revoke (String)
     * @return ResponseEntity with a confirmation message.
     *
     * Example request body:
     * {
     *   "cid": "QmXyz123abc...",
     *   "walletAddress": "0xAbC1234..."
     * }
     */
    @PostMapping("/revoke-access")
    public ResponseEntity<?> revokeAccess(@RequestBody AccessRevokeRequest request){
        accessRightsService.revokeAccess(request.getCid(),request.getWalletAddress());
        return ResponseEntity.ok(Map.of("Message", "Access Revoked Successfully"));
    }

    /**
     * Revokes access for all users on a given CID.
     *
     * @param request RevokeAllAccessRequest with field:
     *                - cid: the content identifier (String)
     * @return ResponseEntity with TransactionResponse containing transaction hash.
     *
     * Example request body:
     * {
     *   "cid": "QmXyz123abc..."
     * }
     */
    @PostMapping("/revoke-all-access")
    public ResponseEntity<TransactionResponse> revokeAllAccess(@RequestBody RevokeAllAccessRequest request){
        String txHash = accessRightsService.revokeAllAccess(request.getCid());
        return ResponseEntity.ok(new TransactionResponse(txHash));
    }

    /**
     * Checks if a wallet address has access to a given CID.
     *
     * @param cid The content identifier (String)
     * @param walletAddress Wallet address to check (String)
     * @return ResponseEntity with AccessCheckResponse indicating access status.
     *
     * Example request:
     * GET /blockchain/has-access?cid=QmXyz123abc...&walletAddress=0xAbC1234...
     */
    @GetMapping("/has-access")
    public ResponseEntity<AccessCheckResponse> hasAccess(@RequestParam String cid, @RequestParam String walletAddress){
        AccessCheckResponse response = accessRightsService.hasAccess(cid, walletAddress);
        return ResponseEntity.ok(response);
    }

    /*
     * File Versioning APIs
     */

    /**
     * Adds a new version for a given file.
     *
     * @param request AddVersionRequest with fields:
     *                - fileId: UUID of the file (String)
     *                - cid: content identifier of the new version (String)
     * @return ResponseEntity with TransactionResponse containing transaction hash.
     *
     * Example request body:
     * {
     *   "fileId": "123e4567-e89b-12d3-a456-426614174000",
     *   "cid": "QmXyz123abc..."
     * }
     */
    @PostMapping("/add-version")
    public ResponseEntity<TransactionResponse> addVersion(@RequestBody AddVersionRequest request){
        String txHash = versioningService.addVersion(request.getFileId(), request.getCid());
        return ResponseEntity.ok(new TransactionResponse(txHash));
    }

    /**
     * Rolls back a file to a previous version.
     *
     * @param request RollbackVersionRequest with fields:
     *                - fileId: UUID of the file (String)
     *                - versionNumber: version number to roll back to (int)
     * @return ResponseEntity with TransactionResponse containing transaction hash.
     *
     * Example request body:
     * {
     *   "fileId": "123e4567-e89b-12d3-a456-426614174000",
     *   "versionNumber": 2
     * }
     */
    @PostMapping("/rollback-version")
    public ResponseEntity<TransactionResponse> rollbackToVersion(@RequestBody RollbackVersionRequest  request){
        String txHash = versioningService.rollbackToVersion(request.getFileId(), request.getVersionNumber());
        return ResponseEntity.ok(new TransactionResponse(txHash));
    }

    /**
     * Gets the full version history for a file.
     *
     * @param fileId UUID of the file (String)
     * @return ResponseEntity with list of VersionInfo objects.
     *
     * Example request:
     * GET /blockchain/version-history?fileId=123e4567-e89b-12d3-a456-426614174000
     */
    @GetMapping("/version-history")
    public ResponseEntity<List<VersionInfo>> getVersionHistory(@RequestParam String fileId) {
        return ResponseEntity.ok(versioningService.getVersionHistory(fileId));
    }

    /**
     * Gets the current version of a file.
     *
     * @param request CurrentVersionRequest with field:
     *                - fileId: UUID of the file (String)
     * @return ResponseEntity with VersionInfo of current version.
     *
     * Example request body:
     * {
     *   "fileId": "123e4567-e89b-12d3-a456-426614174000"
     * }
     */
    @GetMapping("/current-version")
    public ResponseEntity<VersionInfo> getCurrentVersion(@RequestBody CurrentVersionRequest request ){
        VersionInfo versionInfo = versioningService.getCurrentVersion(request.getFileId());
        return ResponseEntity.ok(versionInfo);
    }

    /**
     * Gets a specific version of a file by version number.
     *
     * @param request VersionAtRequest with fields:
     *                - fileId: UUID of the file (String)
     *                - versionNumber: the version number to fetch (int)
     * @return ResponseEntity with VersionInfo of requested version.
     *
     * Example request body:
     * {
     *   "fileId": "123e4567-e89b-12d3-a456-426614174000",
     *   "versionNumber": 1
     * }
     */
    @GetMapping("/version-at")
    public ResponseEntity<VersionInfo> getVersionAt(@RequestBody VersionAtRequest request ){
        VersionInfo version = versioningService.getVersionAt(request.getFileId(), request.getVersionNumber());
        return ResponseEntity.ok(version);
    }

    /**
     * Gets the current status of a file (e.g., active, deleted).
     *
     * @param request FileStatusRequest with field:
     *                - fileId: UUID of the file (String)
     * @return ResponseEntity with FileStatusRespond containing status details.
     *
     * Example request body:
     * {
     *   "fileId": "123e4567-e89b-12d3-a456-426614174000"
     * }
     */
    @PostMapping("/file-status")
    public ResponseEntity<FileStatusRespond> getFileStatus(@RequestBody FileStatusRequest request) {
        FileStatusRespond result = versioningService.getFileStatus(request.getFileId());
        return ResponseEntity.ok(result);
    }

    /**
     * Marks a file as deleted permanently on the blockchain.
     *
     * @param request DeleteFileRequest with field:
     *                - fileId: UUID of the file (String)
     * @return ResponseEntity with TransactionResponse containing transaction hash.
     *
     * Example request body:
     * {
     *   "fileId": "123e4567-e89b-12d3-a456-426614174000"
     * }
     */
    @DeleteMapping("/delete-file")
    public ResponseEntity<TransactionResponse> deleteFile(@RequestBody DeleteFileRequest request){
        String txHash = versioningService.deleteFile(request.getFileId());
        return ResponseEntity.ok(new TransactionResponse(txHash));
    }

    /**
     * Locks a specific version of a file to prevent further changes.
     *
     * @param request LockVersionRequest with fields:
     *                - fileId: UUID of the file (String)
     *                - versionNumber: version to lock (int)
     * @return ResponseEntity with TransactionResponse containing transaction hash.
     *
     * Example request body:
     * {
     *   "fileId": "123e4567-e89b-12d3-a456-426614174000",
     *   "versionNumber": 2
     * }
     */
    @PostMapping("/lock-version")
    public ResponseEntity<TransactionResponse> lockVersion(@RequestBody LockVersionRequest request) {
        String txHash = versioningService.lockVersion(request.getFileId(), request.getVersionNumber());
        return ResponseEntity.ok(new TransactionResponse(txHash));
    }

    /**
     * Checks if a specific version of a file is locked.
     *
     * @param request LockStatusRequest with fields:
     *                - fileId: UUID of the file (String)
     *                - version: version number to check (int)
     * @return ResponseEntity with map {"locked": true/false}.
     *
     * Example request body:
     * {
     *   "fileId": "123e4567-e89b-12d3-a456-426614174000",
     *   "version": 2
     * }
     */
    @PostMapping("/is-version-locked")
    public ResponseEntity<Map<String, Boolean>> isVersionLocked(@RequestBody LockStatusRequest request) {
        boolean locked = versioningService.isVersionLocked(request.getFileId(), request.getVersion());
        return ResponseEntity.ok(Map.of("locked", locked));
    }

    /**
     * Compares two versions of a file to check if they are identical.
     *
     * @param request CompareVersionsRequest with fields:
     *                - fileId: UUID of the file (String)
     *                - version1: first version number (int)
     *                - version2: second version number (int)
     * @return ResponseEntity with map {"isEqual": true/false}.
     *
     * Example request body:
     * {
     *   "fileId": "123e4567-e89b-12d3-a456-426614174000",
     *   "version1": 2,
     *   "version2": 3
     * }
     */
    @PostMapping("/compare-versions")
    public ResponseEntity<Map<String, Boolean>> compareVersions(@RequestBody CompareVersionsRequest request) {
        boolean equal = versioningService.compareVersions(request.getFileId(), request.getVersion1(), request.getVersion2());
        return ResponseEntity.ok(Map.of("isEqual", equal));
    }
}
