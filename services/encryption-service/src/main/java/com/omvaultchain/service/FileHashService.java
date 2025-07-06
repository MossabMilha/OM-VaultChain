package com.omvaultchain.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class FileHashService {
    public String computeSHA256(byte[] Data)throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(Data);
        return bytesToHex(hash);
    }

    public String computeSHA256(Path FilePath) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int  read;
        try (InputStream is = Files.newInputStream(FilePath)) {
            while ((read = is.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        return bytesToHex(digest.digest());
    }

    public String bytesToHex(byte[] hash){
        StringBuilder hex = new StringBuilder();
        for(byte b : hash){
            hex.append(String.format("%02x",b));
        }
        return hex.toString();
    }
}
