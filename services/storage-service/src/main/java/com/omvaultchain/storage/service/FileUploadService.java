package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import com.omvaultchain.storage.model.UploadRequest;
import com.omvaultchain.storage.model.UploadResponse;
import com.omvaultchain.storage.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final MetadataExtractor metadataExtractor;
    private final FileMetadataRepository fileMetadataRepository;
    private final IPFSClient IPFSClient;

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
}
