package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.*;

public @SpringBootTest
class MetadataExtractorIntegrationTest {

    @Autowired
    private MetadataExtractor metadataExtractor;

    @Test
    void shouldExtractMetadataSuccessfully() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files", "example.txt", "text/plain", "Test data 123".getBytes()
        );

        FileMetadata metadata = metadataExtractor.extract(file);
        assertEquals("example.txt", metadata.getFileName());
        assertEquals("text/plain", metadata.getMimeType());
        assertEquals(file.getSize(), metadata.getSize());
    }
}

