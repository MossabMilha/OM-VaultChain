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


            return new UploadResponse(saved.getId().toString(), cid, saved.getFileName(), "UPLOAD_SUCCESS");
        }catch (Exception e){
            throw new RuntimeException("Upload Failed : " + e.getMessage(), e);
        }
    }
    public NewVersionUploadResponse uploadNewFileVersion(NewVersionUploadRequest request, byte[] fileData ){
        try{
            if (fileData == null || fileData.length == 0){
                throw new IllegalArgumentException("Uploaded File Is Empty");
            }
            if (request.getFileName() == null || request.getOwnerId() == null || request.getFileId() == null) {
                throw new IllegalArgumentException("Missing Required Metadata");
            }
            if (request.getIv() == null || request.getEncryptedKey() == null || request.getFileHash() == null) {
                throw new IllegalArgumentException("Missing Required Encryption Metadata");
            }

            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            String encryptedFileName = request.getEncryptedFileName(request.getFileName());
            String cid = IPFSClient.uploadFile(inputStream, encryptedFileName);
            if (cid == null || cid.isEmpty()) {
                throw new RuntimeException("Failed to upload file to IPFS");
            }
            FileMetadata existingFile = fileMetadataRepository.findById(request.getFileId()).orElseThrow(()->new RuntimeException("File not found with ID: " + request.getFileId()));
            existingFile.setCid(cid);
            existingFile.setFileName(request.getFileName());
            existingFile.setMimeType(request.getMimeType());
            existingFile.setIv(request.getIv());
            existingFile.setEncryptedKey(request.getEncryptedKey());
            existingFile.setFileHash(request.getFileHash());

            fileMetadataRepository.save(existingFile);
            return new NewVersionUploadResponse(
                    encryptedFileName,
                    cid,
                    existingFile.getId()
            );


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


}
