package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import com.omvaultchain.storage.model.UploadRequest;
import com.omvaultchain.storage.model.UploadResponse;
import com.omvaultchain.storage.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final IPFSClient ipfsClient;
    private final CIDVerifier cidVerifier;
    private final FileMetadataRepository fileMetadataRepository;
    private final MetadataExtractor metadataExtractor;

    public List<UploadResponse> uploadEncryptedFile(UploadRequest request){
        // Step 1: Decode and write encrypted data to temp file
        byte[] encryptedData = Base64.getDecoder().decode(request.getEncryptedData());
        File tempFile = writeBytesToEncFile(encryptedData,request.getFileName());
        // Step 2: Upload to IPFS
        String cid = ipfsClient.uploadFile(tempFile);
        // Step 3: Verify CID
        cidVerifier.verifyCID(cid);
        // Step 4: Save metadata to DB
        FileMetadata metadata = metadataExtractor.extract(request);
        metadata.setCid(cid);
        fileMetadataRepository.save(metadata);

        // Step 5: Build and return response
        UploadResponse response = new UploadResponse();
        response.setFileName(metadata.getFileName());
        response.setMimeType(metadata.getMimeType());
        response.setSize(metadata.getSize());
        response.setCid(metadata.getCid());
        response.setFileHash(metadata.getFileHash());
        response.setOwnerWallet(metadata.getOwnerWallet());
        response.setUploadAt(metadata.getUploadAt());
        response.setStatus(metadata.getStatus());

        return List.of(response);

    }
    private File writeBytesToEncFile(byte[] data,String originalFileName){
        try{
          String fileName = originalFileName.endsWith(".enc") ? originalFileName : originalFileName + ".enc";
          File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
          try (FileOutputStream fos = new FileOutputStream(tempFile)){
              fos.write(data);
          }
          return tempFile;
        } catch (IOException e){
            throw new RuntimeException("Failed To Write .enc file");
        }
    }
}
