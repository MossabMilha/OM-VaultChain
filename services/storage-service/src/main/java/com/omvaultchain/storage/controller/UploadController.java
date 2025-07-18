package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.BatchUploadResponse;
import com.omvaultchain.storage.model.UploadRequest;
import com.omvaultchain.storage.model.UploadResponse;
import com.omvaultchain.storage.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/storage/upload")
@RequiredArgsConstructor
public class UploadController {
    private final FileUploadService fileUploadService;
    @PostMapping("/single")
    public ResponseEntity<UploadResponse> uploadSingleFile(@RequestParam("ownerId") String ownerId,
                                                           @RequestPart("file") MultipartFile file){
        UploadRequest request = new UploadRequest();
        request.setFileName(file.getOriginalFilename());
        request.setOwnerId(ownerId);
        request.setMimeType(file.getContentType());

        UploadResponse response = fileUploadService.uploadSingleFile(request,file);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<BatchUploadResponse>> uploadBatch(@RequestParam("files") List<MultipartFile> files,
                                                                 @RequestParam("ownerId") String ownerId){
        List<BatchUploadResponse> responses = fileUploadService.uploadBatchFiles(files, ownerId);
        return ResponseEntity.ok(responses);
    }



}
