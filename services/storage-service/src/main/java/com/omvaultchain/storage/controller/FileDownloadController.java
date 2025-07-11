package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.DownloadResponse;
import com.omvaultchain.storage.service.AccessControlValidator;
import com.omvaultchain.storage.service.FileDownloadService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/files")
public class FileDownloadController {
    private final FileDownloadService fileDownloadService;
    private final AccessControlValidator accessControlValidator;

    public FileDownloadController(FileDownloadService fileDownloadService,AccessControlValidator accessControlValidator){
        this.fileDownloadService = fileDownloadService;
        this.accessControlValidator = accessControlValidator;
    }

    @GetMapping("/{cid}")
    public ResponseEntity<DownloadResponse> downloadFile(@PathVariable String cid){
        try{
            String walletAddress = "0xE39544aEFf809062b10Ea7e33a7a392105108976";
             if (!accessControlValidator.hasAccess(walletAddress, cid)) {
                 return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
             }
            byte[] encryptedData = fileDownloadService.downloadFIle(cid);
            String base64Data = Base64.getEncoder().encodeToString(encryptedData);

            return ResponseEntity.ok(new DownloadResponse(base64Data));

        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
