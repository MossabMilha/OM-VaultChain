package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BatchUploaderIntegrationTest {

    @Autowired
    private BatchUploader batchUploader;

    @Test
    void shouldUploadMultipleFilesAndReturnMetadata() throws Exception {
        // Prepare multiple mock files
        MultipartFile file1 = new MockMultipartFile("file", "file1.txt", "text/plain", "First file content".getBytes());
        MultipartFile file2 = new MockMultipartFile("file", "file2.txt", "text/plain", "Second file content".getBytes());
        MultipartFile file3 = new MockMultipartFile("file", "file3.txt", "text/plain", "Third file content".getBytes());

        List<MultipartFile> files = List.of(file1, file2, file3);

        // Upload batch
        List<FileMetadata> results = batchUploader.uploadBatch(files);

        // Assert correct number of results
        assertEquals(3, results.size());

        // Check and print metadata for each file
        for (int i = 0; i < files.size(); i++) {
            FileMetadata metadata = results.get(i);
            String originalFileName = files.get(i).getOriginalFilename();

            assertEquals(originalFileName, metadata.getFileName());
            assertNotNull(metadata.getCid(), "CID should not be null for " + originalFileName);

            // Print metadata
            System.out.println("✅ File: " + metadata.getFileName() + " | CID: " + metadata.getCid());
        }
    }
}
