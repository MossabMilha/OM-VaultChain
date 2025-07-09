package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import com.omvaultchain.storage.service.BatchUploader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FileStorageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BatchUploader batchUploader;

    @Test
    void shouldUploadAndVerifyFilesSuccessfully() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files", "example.txt", "text/plain", "Test data 123".getBytes());

        mockMvc.perform(multipart("/files/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].filename").value("example.txt"))
                .andExpect(jsonPath("$[0].cid", not(emptyString())))
                .andExpect(jsonPath("$[0].size", greaterThan(0)));
    }
}
