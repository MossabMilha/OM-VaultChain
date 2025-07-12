package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
@Service
public class MetadataExtractor {
    private final Tika tika = new Tika();
    public FileMetadata extract(MultipartFile file) throws Exception{
        String fileName = file.getOriginalFilename();
        String mimeType = tika.detect(file.getInputStream());
        long size = file.getSize();
        Instant now = Instant.now();
        return new FileMetadata(fileName,mimeType,size,now);
    }
}
