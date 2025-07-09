package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.FileMetadata;
import com.omvaultchain.storage.service.BatchUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileStorageController {
    private final BatchUploader batchUploader;

    @Autowired
    public FileStorageController(BatchUploader batchUploader){
        this.batchUploader = batchUploader;
    }
    @PostMapping("/upload")
    public ResponseEntity<List<FileMetadata>> uploadFiles(@RequestParam("files")List<MultipartFile> files){
        List<FileMetadata> uploadFiles = batchUploader.uploadBatch(files);
        return ResponseEntity.ok(uploadFiles);
    }

}
