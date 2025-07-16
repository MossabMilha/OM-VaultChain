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
     * @param request FileRegisterRequest containing:
     *                - cid: the IPFS content identifier of the file (String)
     *                - fileHash: the hash of the file content (String)
     * @return ResponseEntity containing a map with the transaction hash key "transactionHash".
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
     * Access Rights Management API
     * */


    /**
     * Retrieves the access list (users who have access) for a given content identifier (CID).
     *
     * @param cid The IPFS content identifier (String)
     * @return ResponseEntity containing a List of AccessRecord for the given CID.
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
     * @param request GrantAccessRequest containing:
     *                - cid: the content identifier (String)
     *                - granteeWallet: the wallet address of the user to grant access (String)
     *                - encryptedKey: the encrypted key for secure access (String)
     * @return ResponseEntity containing a TransactionResponse with the transaction hash.
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
     * Grants access rights to multiple users for a given content identified by CID.
     *
     * @param request GrantMultipleAccessRequest containing:
     *                - cid: the content identifier for which access is granted
     *                - userId: a list of maps where each map contains:
     *                    - "walletAddress": the user's wallet address (as a String)
     *                    - "encryptedKey": the encrypted key for the user (as a String)
     * @return ResponseEntity containing a TransactionResponse with the transaction hash
     *         of the access granting operation on the blockchain or smart contract.
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
     * @param request AccessRevokeRequest containing:
     *                - cid: the content identifier (String)
     *                - walletAddress: the wallet address to revoke (String)
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
     * @param request RevokeAllAccessRequest containing:
     *                - cid: the content identifier (String)
     * @return ResponseEntity containing a TransactionResponse with the transaction hash.
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
     * Checks if a specific wallet address has access to a content identified by CID.
     *
     * @param cid The content identifier (String)
     * @param walletAddress The wallet address to check access for (String)
     * @return ResponseEntity containing AccessCheckResponse with access info.
     *
     * Example request:
     * GET /blockchain/has-access?cid=QmXyz123abc...&walletAddress=0xAbC1234...
     */
    @GetMapping("/has-access")
    public ResponseEntity<AccessCheckResponse> hasAccess(@RequestParam String cid, @RequestParam String walletAddress){
        AccessCheckResponse response = accessRightsService.hasAccess(cid, walletAddress);
        return ResponseEntity.ok(response);
    }

    /* File Versioning */
    /**
     * Add a new version for a given file.
     *
     * @param request AddVersionRequest containing fileId and cid (new file version on IPFS)
     * @return Transaction hash of the addVersion operation on blockchain
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
     * Roll back a file to a previous version.
     *
     * @param request RollbackVersionRequest containing fileId and versionNumber
     * @return Transaction hash of the rollback operation
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
     * Get version history of a file.
     *
     * @param fileId The UUID of the file
     * @return List of all versions (cid, version number, timestamp)
     *
     * Example: /blockchain/version-history?fileId=123e4567-e89b-12d3-a456-426614174000
     */
    @GetMapping("/version-history")
    public ResponseEntity<List<VersionInfo>> getVersionHistory(@RequestParam String fileId) {
        return ResponseEntity.ok(versioningService.getVersionHistory(fileId));
    }
    /**
     * Get the current version of a file.
     *
     * @param request CurrentVersionRequest containing fileId
     * @return The current VersionInfo
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
     * Get a specific version by version number.
     *
     * @param request VersionAtRequest with fileId and versionNumber
     * @return The corresponding VersionInfo
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
     * Get the current file status (e.g., active, deleted).
     *
     * @param request FileStatusRequest with fileId
     * @return FileStatusRespond with status details
     *
     * Example request body:
     * {
     *   "fileId": "123e4567-e89b-12d3-a456-426614174000"
     * }
     */
    @GetMapping("/file-status")
    public ResponseEntity<FileStatusRespond> getFileStatus(@RequestBody FileStatusRequest request) {
        FileStatusRespond result = versioningService.getFileStatus(request.getFileId());
        return ResponseEntity.ok(result);
    }
    /**
     * Permanently mark a file as deleted on the blockchain.
     *
     * @param request DeleteFileRequest with fileId
     * @return Transaction hash of the delete operation
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


}
