package com.omvaultchain.storage.service;

import org.springframework.stereotype.Service;

@Service
public class FileDownloadService {
    private final IPFSClient ipfsClient;
    private final CIDVerifier cidVerifier;
    public FileDownloadService(IPFSClient ipfsClient,CIDVerifier cidVerifier){
        this.ipfsClient = ipfsClient;
        this.cidVerifier = cidVerifier;
    }
    public byte[] downloadFIle(String cid){
        //Verify CID format
        cidVerifier.verifyCID(cid);

        //Download from IPFS
        return ipfsClient.DownloadData(cid);
    }
}
