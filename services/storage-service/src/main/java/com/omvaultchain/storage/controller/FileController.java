//package com.omvaultchain.storage.controller;
//
//import com.omvaultchain.storage.model.FileMetadata;
//import com.omvaultchain.storage.repository.FileMetadataRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/storage/files")
//@RequiredArgsConstructor
//public class FileController {
//    private final FileMetadataRepository fileMetadataRepository;
//
//    /**
//     * Returns all files owned by the authenticated user.
//     *
//     * Only returns files where the current user is the owner.
//     *
//     * @param userId User ID extracted from the request header or JWT
//     * @return List of FileMetadata objects owned by the user
//     */
//    @GetMapping("/owned")
//    public ResponseEntity<List<FileMetadata>> getOwnedFiles(@RequestHeader("userId") String userId) {
//        List<FileMetadata> files = fileMetadataRepository.findByOwnerIdAndDeletedFalse(userId);
//        return ResponseEntity.ok(files);
//    }
//
//    /**
//     * Returns all files shared with the authenticated user.
//     *
//     * Only returns files where the current user is in the access control list
//     * and is not the owner.
//     *
//     * Requires implementation of access_permissions table or blockchain lookup.
//     *
//     * @param userId User ID extracted from the request header or JWT
//     * @return List of FileMetadata objects accessible by the user
//     */
//    @GetMapping("/accessed")
//    public ResponseEntity<List<FileMetadata>> getAccessedFiles(@RequestHeader("userId") String userId){
//        // No access control implemented yet — return empty list
//        return ResponseEntity.ok(List.of());
//    }
//    /**
//     * Returns all files available to the authenticated user, including:
//     * - Files they own
//     * - Files shared with them
//     *
//     * Combines ownership and access list.
//     *
//     * Sharing/access logic requires access_permissions or smart contract check.
//     *
//     * @param userId User ID extracted from the request header or JWT
//     * @return List of FileMetadata objects the user can access
//     */
//
//}
