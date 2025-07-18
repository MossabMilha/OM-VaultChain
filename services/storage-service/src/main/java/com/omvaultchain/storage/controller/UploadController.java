package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.UploadRequest;
import com.omvaultchain.storage.model.UploadResponse;
import com.omvaultchain.storage.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage/upload")
@RequiredArgsConstructor
public class UploadController {
    private final FileUploadService fileUploadService;
    @PostMapping("/single")
    public ResponseEntity<UploadResponse> uploadSingleFile(@RequestParam("fileName") String fileName,
                                                           @RequestParam("ownerId") String ownerId,
                                                           @RequestParam(value = "mimeType", required = false) String mimeType,
                                                           @RequestPart("file") MultipartFile file){
        UploadRequest request = new UploadRequest();
        request.setFileName(fileName);
        request.setOwnerId(ownerId);
        request.setMimeType(mimeType);

        UploadResponse response = fileUploadService.uploadSingleFile(request,file);
        return ResponseEntity.ok(response);
    }


}
