package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.*;
import com.omvaultchain.storage.repository.FileMetadataRepository;
import com.omvaultchain.storage.repository.UploadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final MetadataExtractor metadataExtractor;
    private final FileMetadataRepository fileMetadataRepository;
    private final IPFSClient IPFSClient;
    private final UploadStatusRepository uploadStatusRepository;

    public UploadResponse uploadSingleFile(UploadRequest request, MultipartFile file){
        try{
            if(file.isEmpty()){
                throw new IllegalArgumentException("Uploaded File Is Empty");
            }
            if (request.getFileName()== null || request.getOwnerId() == null){
                throw new IllegalArgumentException("Missing Required Metadata");
            }
            FileMetadata metadata = metadataExtractor.extract(file);
            metadata.setId(UUID.randomUUID().toString());
            metadata.setFileName(request.getFileName());
            metadata.setMimeType(request.getMimeType()!=null?request.getMimeType():file.getContentType());
            metadata.setOwnerId(request.getOwnerId());
            metadata.setCreatedAt(Instant.now());
            metadata.setUpdatedAt(Instant.now());
            metadata.setDeleted(false);

            String cid = IPFSClient.uploadFile(file.getInputStream(), request.getEncryptedFileName(request.getFileName()));
            metadata.setCid(cid);
            FileMetadata saved = fileMetadataRepository.save(metadata);
            return new UploadResponse(saved.getId().toString(), cid, saved.getFileName(), "UPLOAD_SUCCESS");
        }catch (Exception e){
            throw new RuntimeException("Upload Failed : " + e.getMessage(), e);
        }
    }
    public List<BatchUploadResponse> uploadBatchFiles(List<MultipartFile> files, String ownerId) {
        List<BatchUploadResponse> results = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                UploadRequest request = new UploadRequest();
                request.setOwnerId(ownerId);
                request.setFileName(file.getOriginalFilename());
                request.setMimeType(file.getContentType());

                UploadResponse singleResponse = uploadSingleFile(request, file);

                results.add(new BatchUploadResponse(
                        singleResponse.getFileName(),
                        singleResponse.getCid(),
                        singleResponse.getStatus(),
                        "Uploaded successfully"
                ));

            } catch (Exception e) {
                results.add(new BatchUploadResponse(
                        file.getOriginalFilename(),
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
