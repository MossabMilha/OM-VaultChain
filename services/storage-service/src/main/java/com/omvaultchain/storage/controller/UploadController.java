package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.*;
import com.omvaultchain.storage.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/storage/upload")
@RequiredArgsConstructor
public class UploadController {
    private final FileUploadService fileUploadService;
    /**
     * Uploads a single encrypted file to decentralized storage (IPFS).
     *
     * @param ownerId The unique ID of the user uploading the file. Sent as a form-data field.
     * @param file    The file to upload. Sent as a form-data file part.
     * @return ResponseEntity containing UploadResponse with:
     *         - fileName: name of the uploaded file (String)
     *         - cid: content identifier from IPFS (String)
     *         - fileId: internal unique file ID (String)
     *         - uploadId: ID used for tracking the upload (String)
     *
     * Example form-data:
     * - ownerId: "user-123"
     * - file: [choose file]
     *
     * Example response:
     * {
     *   "fileName": "doc.pdf",
     *   "cid": "Qmabc123xyz...",
     *   "fileId": "f0837c2e-b21d-44f0-8f2a-b1d02ccfa882",
     *   "uploadId": "upload-abc123"
     * }
     */
    @RequestMapping(value = "/single", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/single")
    public ResponseEntity<UploadResponse> uploadSingleFile(@RequestBody UploadRequest request){
        byte[] fileData = Base64.getDecoder().decode(request.getFileData());


        UploadResponse response = fileUploadService.uploadSingleFile(request,fileData);
        return ResponseEntity.ok(response);
    }
    /**
     * Uploads multiple encrypted files to decentralized storage (IPFS) in a batch.
     *
     * @param files   A list of files to upload. Each is sent as a form-data file part named "files".
     * @param ownerId The unique ID of the user uploading the files. Sent as a form-data field.
     * @return ResponseEntity containing a list of BatchUploadResponse objects, each with:
     *         - fileName: name of the file (String)
     *         - cid: content identifier from IPFS (String)
     *         - fileId: internal unique file ID (String)
     *         - status: upload status ("COMPLETED", "FAILED") (String)
     *
     * Example form-data:
     * - ownerId: "user-123"
     * - files: [select multiple files with same name key `files`]
     *
     * Example response:
     * [
     *   {
     *     "fileName": "doc1.pdf",
     *     "cid": "Qmabc123xyz...",
     *     "fileId": "file-id-1",
     *     "status": "COMPLETED"
     *   },
     *   {
     *     "fileName": "image.png",
     *     "cid": "Qmdef456uvw...",
     *     "fileId": "file-id-2",
     *     "status": "COMPLETED"
     *   }
     * ]
     */
    //    @PostMapping("/batch")
    //    public ResponseEntity<List<BatchUploadResponse>> uploadBatch(@RequestParam("files") List<MultipartFile> files,
    //                                                                 @RequestParam("ownerId") String ownerId){
    //        List<BatchUploadResponse> responses = fileUploadService.uploadBatchFiles(files, ownerId);
    //        return ResponseEntity.ok(responses);
    //    }

    /**
     * Retrieves the current status of an ongoing or completed upload using its upload ID.
     *
     * @param request UploadStatusRequest containing:
     *                - uploadId: unique identifier for the upload (String)
     * @return ResponseEntity containing UploadStatusResponse with:
     *         - uploadId: ID of the upload (String)
     *         - fileName: name of the file (String)
     *         - uploadedBytes: number of bytes uploaded so far (long)
     *         - totalBytes: total size of the file in bytes (long)
     *         - status: upload status ("IN_PROGRESS", "COMPLETED", "CANCELLED") (String)
     *         - startedAt: timestamp when upload started (Instant)
     *
     * Example request body:
     * {
     *   "uploadId": "upload-abc123"
     * }
     *
     * Example response:
     * {
     *   "uploadId": "upload-abc123",
     *   "fileName": "doc.pdf",
     *   "uploadedBytes": 10485760,
     *   "totalBytes": 10485760,
     *   "status": "COMPLETED",
     *   "startedAt": "2025-07-19T14:30:00Z"
     * }
     */
//    @PostMapping("/status")
//    public ResponseEntity<UploadStatusResponse> getUploadStatus(@RequestBody UploadStatusRequest request){
//        UploadStatusResponse response = fileUploadService.getUploadStatus(request.getUploadId());
//        return ResponseEntity.ok(response);
//    }

















    @PostMapping("/resume")
    public ResponseEntity<ResumeUploadResponse> resumeUpload(@RequestParam("uploadId") String uploadId, @RequestPart("file") MultipartFile file){
        ResumeUploadResponse response = fileUploadService.resumeUpload(uploadId,file);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/cancel")
    public ResponseEntity<GenericResponse> cancelUpload(@RequestBody CancelUploadRequest request){
        fileUploadService.cancelUpload(request.getUploadId());
        return ResponseEntity.ok(new GenericResponse("Upload cancelled successfully"));
    }





}
