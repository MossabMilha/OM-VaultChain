package com.omvaultchain.storage.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@RequiredArgsConstructor
public class FileDownloadService {
    private final IPFSClient ipfsClient;
    private final AccessControlValidator accessControlValidator;
    public void downloadFile(String cid, String walletAddress, HttpServletResponse response)throws IOException{
        // 1. Check Access Permission
        if(!accessControlValidator.hasAccess(cid,walletAddress)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"You Don't Have Access To This File");
            return;
        }

        // 2. Fetch encrypted file from IPFS
        File encryptedFile = ipfsClient.DownloadFile(cid);

        //3. Set Http headers
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment;filename\""+encryptedFile.getName()+"\"");

        //4.Stream file content to response
        try(InputStream input = new FileInputStream(encryptedFile); OutputStream output = response.getOutputStream()){
            byte[] buffer = new byte[8192];
            int length;
            while((length = input.read(buffer)) != -1){
                output.write(buffer,0,length);
            }
        }


    }
}
