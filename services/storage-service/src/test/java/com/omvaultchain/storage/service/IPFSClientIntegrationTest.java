package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.PinataUploadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class IPFSClientIntegrationTest {

    @Autowired
    private IPFSClient ipfsClient;

    @Test
    public void testRealPinataUpload() throws Exception {
        // Create a test file
        MockMultipartFile file = new MockMultipartFile("file", "test-file.txt", "text/plain", "Hello, IPFS!".getBytes());

        // Call the real Pinata API
        String cid = ipfsClient.UploadData(file);

        System.out.println("Uploaded File CID: " + cid);

        // Assert we received a non-null CID
        assertNotNull(cid);
    }
    @Test
    public void testRealPinataDownload()throws Exception{
        byte[] Data = ipfsClient.DownLoadData("QmZaEQdUN9aj2UXzac2QHgDq5k2PHm6kMi17zJpRwwgKnm");
        String result = new String(Data, StandardCharsets.UTF_8);
        System.out.println("Uploaded File CID: " + result);
    }
}
