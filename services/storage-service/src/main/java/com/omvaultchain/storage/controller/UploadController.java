package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.*;
import com.omvaultchain.storage.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/storage/upload")
@RequiredArgsConstructor
public class UploadController {
    private final FileUploadService fileUploadService;
    /**
     * Uploads a single encrypted file to decentralized storage (IPFS).
     *
     * The request body should contain a JSON object with the following fields:
     * - ownerId: Unique ID of the user uploading the file (String).
     * - fileData: Base64-encoded content of the encrypted file (String).
     * - fileName: Original file name (String).
     * - mimeType: MIME type of the file (String).
     * - iv: Initialization vector used for encryption (String).
     * - encryptedKey: The encrypted AES key used to encrypt the file (String).
     * - fileHash: Hash of the original file content for integrity verification (String).
     *
     * The uploaded file will be stored with ".enc" appended to the base file name.
     *
     * Example request body:
     * {
     *   "ownerId": "user-123",
     *   "fileData": "BASE64_ENCODED_ENCRYPTED_FILE_CONTENT",
     *   "fileName": "document.pdf",
     *   "mimeType": "application/pdf",
     *   "iv": "randomIVvalue",
     *   "encryptedKey": "encryptedAESKeyValue",
     *   "fileHash": "sha256HashOfOriginalFile"
     * }
     *
     * Example response:
     * {
     *   "fileName": "document.enc",
     *   "cid": "Qmabc123xyz...",
     *   "fileId": "f0837c2e-b21d-44f0-8f2a-b1d02ccfa882",
     *   "uploadId": "upload-abc123"
     * }
     */


    @PostMapping("/single")
    public ResponseEntity<UploadResponse> uploadSingleFile(@RequestBody UploadRequest request){
            byte[] fileData = Base64.getDecoder().decode(request.getFileData());
            UploadResponse response = fileUploadService.uploadSingleFile(request, fileData);
            return ResponseEntity.ok(response);

    }
    /**
     * Uploads multiple encrypted files to decentralized storage (IPFS) in a batch.
     *
     * The request body should contain a JSON object with the following fields:
     * - ownerId: Unique ID of the user uploading the files (String).
     * - files: A list of file objects, each containing:
     *      - fileData: Base64-encoded content of the encrypted file (String).
     *      - fileName: Original file name (String).
     *      - mimeType: MIME type of the file (String).
     *      - iv: Initialization vector used for encryption (String).
     *      - encryptedKey: The encrypted AES key used to encrypt the file (String).
     *      - fileHash: Hash of the original file content for integrity verification (String).
     *
     * Each uploaded file will be stored with ".enc" appended to its base file name.
     *
     * Example request body:
     * {
     *   "ownerId": "user-123",
     *   "files": [
     *     {
     *       "fileData": "BASE64_ENCODED_ENCRYPTED_FILE_1",
     *       "fileName": "doc1.pdf",
     *       "mimeType": "application/pdf",
     *       "iv": "randomIV1",
     *       "encryptedKey": "encryptedAESKey1",
     *       "fileHash": "sha256Hash1"
     *     },
     *     {
     *       "fileData": "BASE64_ENCODED_ENCRYPTED_FILE_2",
     *       "fileName": "image.png",
     *       "mimeType": "image/png",
     *       "iv": "randomIV2",
     *       "encryptedKey": "encryptedAESKey2",
     *       "fileHash": "sha256Hash2"
     *     }
     *   ]
     * }
     *
     * Example response:
     * [
     *   {
     *     "fileName": "doc1.enc",
     *     "cid": "Qmabc123xyz...",
     *     "fileId": "file-id-1",
     *     "status": "COMPLETED"
     *   },
     *   {
     *     "fileName": "image.enc",
     *     "cid": "Qmdef456uvw...",
     *     "fileId": "file-id-2",
     *     "status": "COMPLETED"
     *   }
     * ]
     */
    @PostMapping("/batch")
    public ResponseEntity<List<BatchUploadResponse>> uploadBatch(@RequestBody BatchUploadRequest request) {
        String ownerId = request.getOwnerId();
        List<BatchUploadRequest.FileData> files = request.getFiles();
        List<BatchUploadResponse> responses = fileUploadService.uploadBatchFiles(files, ownerId);
        return ResponseEntity.ok(responses);
    }

}
