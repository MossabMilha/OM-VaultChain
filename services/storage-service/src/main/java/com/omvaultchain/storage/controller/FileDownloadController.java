package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.DownloadResponse;
import com.omvaultchain.storage.service.AccessControlValidator;
import com.omvaultchain.storage.service.FileDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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


}
