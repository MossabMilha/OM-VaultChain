package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.UploadRequest;
import com.omvaultchain.storage.model.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service

public class FileUploadService {

    private final IPFSClient ipfsClient;
    private final CIDVerifier cidVerifier;

    public FileUploadService(IPFSClient ipfsClient, CIDVerifier cidVerifier) {
        this.ipfsClient = ipfsClient;
        this.cidVerifier = cidVerifier;
    }

    public List<UploadResponse> uploadEncryptedFile(UploadRequest request){

        byte[] encryptedData = Base64.getDecoder().decode(request.getEncryptedData());
        File tempFile = writeBytesToEncFile(encryptedData,request.getFileName());
        String cid = ipfsClient.uploadFile(tempFile);
        cidVerifier.verifyCID(cid);
        UploadResponse response = new UploadResponse();
        response.setFileName(request.getFileName());
        response.setMimeType(request.getMimeType());
        response.setSize(request.getSize());
        response.setCid(cid);
        response.setFileHash(request.getHashedData());
        response.setOwnerWallet("0xE39544aEFf809062b10Ea7e33a7a392105108976");
        response.setUploadAt(Instant.now());
        response.setStatus("COMPLETED");

        return List.of(response);

    }
    private File writeBytesToEncFile(byte[] data,String originalFileName){
        try{
          String fileName = originalFileName+".enc";
          File tempFile = File.createTempFile(fileName,null);
          try (FileOutputStream fos = new FileOutputStream(tempFile)){
              fos.write(data);
          }
          return tempFile;
        } catch (IOException e){
            throw new RuntimeException("Failed To Write .enc file");
        }
    }
}
