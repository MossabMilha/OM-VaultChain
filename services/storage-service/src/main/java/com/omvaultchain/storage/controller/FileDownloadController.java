package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.DownloadResponse;
import com.omvaultchain.storage.service.AccessControlValidator;
import com.omvaultchain.storage.service.FileDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileDownloadController {
    private final FileDownloadService fileDownloadService;
    @GetMapping("/download/{cid}")
    @ResponseStatus(HttpStatus.OK)
    public void downloadEncryptedFile(@PathVariable String cid, @RequestHeader("Wallet") String walletAddress, HttpServletResponse response)throws IOException{
        fileDownloadService.downloadFile(cid,walletAddress,response);
    }
    @GetMapping("/download-bytes")
    public ResponseEntity<byte[]> downloadEncryptedBytes(@RequestParam String cid, @RequestParam String walletAddress) throws IOException {

        byte[] encryptedData = fileDownloadService.downloadEncryptedFileAsBytes(cid, walletAddress);

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"" + cid + ".enc\"").contentType(MediaType.APPLICATION_OCTET_STREAM).body(encryptedData);
    }
    @GetMapping("/download-base64")
    public ResponseEntity<String> downloadEncryptedFileBase64(
            @RequestParam String cid,
            @RequestParam String walletAddress) throws IOException {

        byte[] encryptedData = fileDownloadService.downloadEncryptedFileAsBytes(cid, walletAddress);
        String encodedBase64 = Base64.getEncoder().encodeToString(encryptedData);

        return ResponseEntity.ok(encodedBase64);
    }

}
