package com.omvaultchain.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Service
public class CIDVerifier {
    @Autowired
    private FileHashService FileHash_Service;

    public boolean verifyCID(byte[] Data,String expectedCID)throws Exception{
        String hash = FileHash_Service.computeSHA256(Data);
        return expectedCID.equals(hash);
    }
}
