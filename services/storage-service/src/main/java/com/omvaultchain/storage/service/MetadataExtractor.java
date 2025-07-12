package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import com.omvaultchain.storage.model.UploadRequest;
import com.omvaultchain.storage.model.UploadResponse;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
@Service
public class MetadataExtractor {
    private final Tika tika = new Tika();

    public FileMetadata extract(UploadRequest request){
        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(request.getFileName());
        metadata.setMimeType(request.getMimeType());
        metadata.setSize(request.getSize());

        metadata.setFileHash(request.getHashedData());
        metadata.setOwnerWallet(request.getOwnerWallet());
        metadata.setUploadAt(Instant.now());
        metadata.setStatus("COMPLETED");

        return metadata;
    }
}
