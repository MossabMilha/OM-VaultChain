package com.omvaultchain.storage.service;

import org.springframework.stereotype.Service;

@Service
public class BatchUploader {
//
//    private final IPFSClient ipfsClient;
//    private final MetadataExtractor metadataExtractor;
//
//
//    public BatchUploader(IPFSClient ipfsClient, MetadataExtractor metadataExtractor) {
//        this.ipfsClient = ipfsClient;
//        this.metadataExtractor = metadataExtractor;
//
//    }
//    public List<FileMetadata> uploadBatch(List<MultipartFile> files){
//        List<FileMetadata> fileMetadataList = new ArrayList<>();
//        for (MultipartFile file : files){
//            try {
//                FileMetadata metadata = metadataExtractor.extract(file);
//                String cid = ipfsClient.UploadData(file);
//                metadata.setCid(cid);
//                fileMetadataList.add(metadata);
//            }catch (Exception e){
//                throw new RuntimeException("Batch upload failed for file: " + file.getOriginalFilename(), e);
//            }
//        }
//        return fileMetadataList;
//    }

}
