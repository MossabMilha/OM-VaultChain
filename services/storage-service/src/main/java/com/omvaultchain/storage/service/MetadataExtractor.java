package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class MetadataExtractor {
    public FileMetadata extract(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        byte[] fileBytes = file.getBytes();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(fileBytes);
        String fileHash = bytesToHex(hashBytes);
        FileMetadata metadata = new FileMetadata();
        metadata.setSizeBytes(file.getSize());
        metadata.setFileHash(fileHash);
        return metadata;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
