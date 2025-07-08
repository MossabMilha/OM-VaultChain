package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class BatchUploader {

    private final IPFSClient ipfsClient;
    private final MetadataExtractor metadataExtractor;
    private final CIDVerifier cidVerifier;

    public BatchUploader(IPFSClient ipfsClient, MetadataExtractor metadataExtractor, CIDVerifier cidVerifier) {
        this.ipfsClient = ipfsClient;
        this.metadataExtractor = metadataExtractor;
        this.cidVerifier = cidVerifier;
    }
    public List<FileMetadata> uploadBatch(List<MultipartFile> files){
        List<FileMetadata> fileMetadataList = new ArrayList<>();
        for (MultipartFile file : files){
            try {
                FileMetadata metadata = metadataExtractor.extract(file);
                String cid = ipfsClient.UploadData(file);
                cidVerifier.verifyCID(file.getBytes(),cid);
                metadata.setCid(cid);
                fileMetadataList.add(metadata);
            }catch (Exception e){
                throw new RuntimeException("Batch upload failed for file: " + file.getOriginalFilename(), e);
            }
        }
        return fileMetadataList;
    }

}
