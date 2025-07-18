package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.BatchUploadResponse;
import com.omvaultchain.storage.model.FileMetadata;
import com.omvaultchain.storage.model.UploadRequest;
import com.omvaultchain.storage.model.UploadResponse;
import com.omvaultchain.storage.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
}
