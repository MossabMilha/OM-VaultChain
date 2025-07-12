package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.UploadRequest;
import com.omvaultchain.storage.model.UploadResponse;
import com.omvaultchain.storage.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/files")

public class FileStorageController {
    private final FileUploadService fileUploadService;
    @Autowired
    public FileStorageController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<UploadResponse>> uploadEncryptedFile(@RequestBody UploadRequest request) {
        List<UploadResponse> responses = fileUploadService.uploadEncryptedFile(request);
        return ResponseEntity.ok(responses);
    }

}
