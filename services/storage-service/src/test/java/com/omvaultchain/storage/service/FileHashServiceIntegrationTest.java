package com.omvaultchain.storage.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FileHashServiceIntegrationTest {

    @Autowired
    private FileHashService fileHashService;

    @Test
    void shouldComputeCorrectSHA256() throws Exception {
        String content = "Hash this data";
        byte[] input = content.getBytes();

        String hash = fileHashService.computeSHA256(input);

        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256 = 64 hex chars
    }
}

