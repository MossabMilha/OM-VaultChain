package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.*;
import com.omvaultchain.storage.repository.AccessPermissionRepository;
import com.omvaultchain.storage.repository.FileMetadataRepository;
import com.omvaultchain.storage.repository.UploadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final MetadataExtractor metadataExtractor;

    private final IPFSClient IPFSClient;
    private final UploadStatusRepository uploadStatusRepository;
    private final FileMetadataRepository fileMetadataRepository;
    private final AccessPermissionRepository accessPermissionRepository;

    public UploadResponse uploadSingleFile(UploadRequest request, byte[] fileData ){
        try{
            if(fileData == null || fileData.length == 0){
                throw new IllegalArgumentException("Uploaded File Is Empty");
            }
            if (request.getFileName()== null || request.getOwnerId() == null){
                throw new IllegalArgumentException("Missing Required Metadata");
            }
            if (request.getIv() == null || request.getEncryptedKey() == null || request.getFileHash() == null){
                throw new IllegalArgumentException("Missing Required Encryption Metadata");
            }
            


            FileMetadata metadata = new FileMetadata();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            String cid = IPFSClient.uploadFile(inputStream, request.getEncryptedFileName(request.getFileName()));

            // Save Metadata
            metadata.setId(UUID.randomUUID().toString());
            metadata.setFileName(request.getFileName());
            metadata.setMimeType(request.getMimeType());
            metadata.setOwnerId(request.getOwnerId());
            metadata.setSizeBytes((long) fileData.length);
            metadata.setCreatedAt(Instant.now());
            metadata.setUpdatedAt(Instant.now());
            metadata.setDeleted(false);
            metadata.setIv(request.getIv());
            metadata.setEncryptedKey(request.getEncryptedKey());
            metadata.setFileHash(request.getFileHash());
            metadata.setEncryptionAlgorithm("AES-256-GCM");

            metadata.setCid(cid);
            FileMetadata saved = fileMetadataRepository.save(metadata);

            // Grant access to The owner (create access permission)
            AccessPermission permission = new AccessPermission();
            permission.setId(UUID.randomUUID().toString());
            permission.setFileId(saved.getId());
            permission.setUserId(request.getOwnerId());
            permission.setEncryptedKey(request.getEncryptedKey());
            permission.setCreatedAt(Instant.now());
            permission.setIsActive(true);
            accessPermissionRepository.save(permission);


            return new UploadResponse(saved.getId().toString(), cid, saved.getFileName(), "UPLOAD_SUCCESS");
        }catch (Exception e){
            throw new RuntimeException("Upload Failed : " + e.getMessage(), e);
        }
    }
    public List<BatchUploadResponse> uploadBatchFiles(List<BatchUploadRequest.FileData> files, String ownerId) {
        List<BatchUploadResponse> results = new ArrayList<>();

        for (BatchUploadRequest.FileData file : files) {
            try {
                UploadRequest request = new UploadRequest();
                byte[] fileData = Base64.getDecoder().decode(file.getFileData());

                request.setOwnerId(ownerId);
                request.setFileName(file.getFileName());
                request.setMimeType(file.getMimeType());
                request.setFileData(file.getFileData());
                request.setIv(file.getIv());
                request.setEncryptedKey(file.getEncryptedKey());
                request.setFileHash(file.getFileHash());


                UploadResponse singleResponse = uploadSingleFile(request, fileData);

                results.add(new BatchUploadResponse(
                        singleResponse.getFileName(),
                        singleResponse.getCid(),
                        singleResponse.getStatus(),
                        "Uploaded successfully"
                ));
            } catch (Exception e) {
                results.add(new BatchUploadResponse(
                        file.getFileName(),
                        null,
                        "UPLOAD_FAILED",
                        e.getMessage()
                ));
            }
        }

        return results;
    }


    public ResumeUploadResponse  resumeUpload(String uploadId, MultipartFile newPart){
        UploadStatus status = uploadStatusRepository.findById(uploadId).orElseThrow(()->new IllegalArgumentException("Upload ID Not Found"));
        if (status.isCanceled()){
            throw new IllegalArgumentException("Upload Is Canceled And Cannot Be Resumed.");
        }
        if(status.isCompleted()){
            throw new IllegalStateException("Upload Already Completed");
        }
        Path tempFilePath = Paths.get("/tmp/uploads",uploadId+".part");
        File tempFile = tempFilePath.toFile();
        try{
            try(OutputStream out = new FileOutputStream(tempFile, true)) {
                out.write(newPart.getBytes());
            }
            long newUploadedBytes = status.getUploadedBytes() + newPart.getSize();
            status.setUploadedBytes(newUploadedBytes);
            if (newUploadedBytes >= status.getTotalBytes()) {
                String cid = IPFSClient.uploadFile(tempFile);
                status.setCompleted(true);
                uploadStatusRepository.save(status);
                tempFile.delete();
                return ResumeUploadResponse.builder()
                        .fileId(status.getUploadId())
                        .fileName(status.getFileName())
                        .status("COMPLETED")
                        .uploadedBytes(newUploadedBytes)
                        .totalBytes(status.getTotalBytes())
                        .cid(cid)
                        .build();
            }else{
                uploadStatusRepository.save(status);
                return ResumeUploadResponse .builder()
                        .fileId(status.getUploadId())
                        .fileName(status.getFileName())
                        .status("IN_PROGRESS")
                        .uploadedBytes(newUploadedBytes)
                        .totalBytes(status.getTotalBytes())
                        .cid(null)
                        .build();
            }
        }catch (IOException e){
            throw new RuntimeException("Failed To Resume Upload",e);
        }
    }

    public UploadStatusResponse getUploadStatus(String uploadId){
        Optional<UploadStatus> statusOpt = uploadStatusRepository.findById(uploadId);
        if (statusOpt.isEmpty()){
            throw new RuntimeException("Upload Id Not Found");
        }
        UploadStatus status = statusOpt.get();
        String inferredStatus;
        if (status.isCanceled()) {
            inferredStatus = "CANCELLED";
        } else if (status.isCompleted()) {
            inferredStatus = "COMPLETED";
        } else {
            inferredStatus = "IN_PROGRESS";
        }
        UploadStatusResponse response = new UploadStatusResponse();
        response.setUploadId(status.getUploadId());
        response.setFileName(status.getFileName());
        response.setUploadedBytes(status.getUploadedBytes());
        response.setTotalBytes(status.getTotalBytes());
        response.setStatus(inferredStatus);
        response.setStartedAt(status.getStartedAt());

        return response;
    }
    public void cancelUpload(String uploadId){
        Optional<UploadStatus> statusOpt = uploadStatusRepository.findById(uploadId);
        if(statusOpt.isEmpty()){
            throw new RuntimeException("Upload ID Not Found : " + uploadId);
        }
        UploadStatus status = statusOpt.get();
        if(!status.isCompleted() && !status.isCanceled()){
            status.setCanceled(true);
            uploadStatusRepository.save(status);
        }
    }
}
