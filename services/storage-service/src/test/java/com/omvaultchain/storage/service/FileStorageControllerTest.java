package com.omvaultchain.storage.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class FileStorageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BatchUploader batchUploader;

    @Test
    void shouldUploadRealFileFromDisk() throws Exception {
        Path path = Paths.get("C:/Users/PC/Downloads/complete_readme.md");
        byte[] data = Files.readAllBytes(path);

        MockMultipartFile file = new MockMultipartFile(
                "files",
                path.getFileName().toString(),
                Files.probeContentType(path),  // detects MIME type
                data);

        mockMvc.perform(multipart("/files/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fileName").value(path.getFileName().toString()))
                .andExpect(jsonPath("$[0].cid", not(emptyString())))
                .andExpect(jsonPath("$[0].size", greaterThan(0)));
    }
}
