package com.omvaultchain.blockchain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileRegistryService {
    @Autowired
    private SmartContractClient smartContractClient;

    public String registerFileOnBlockChain(String cid, String fileHash) {
        try {
            return smartContractClient.registerFile(cid,fileHash);
        }catch (Exception e){
            throw new RuntimeException("BlockChain Registration Failed : " +e.getMessage());
        }
    }
}
