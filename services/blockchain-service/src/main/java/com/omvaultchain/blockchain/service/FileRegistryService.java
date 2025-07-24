package com.omvaultchain.blockchain.service;

import com.omvaultchain.blockchain.contracts.VersionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileRegistryService {
    @Autowired
    private SmartContractClient smartContractClient;
    @Autowired
    private VersionManager versionManager;

    public String registerFileOnBlockChain(String ownerId,String cid, String fileHash,long version) {
        try {
            return smartContractClient.registerFile(ownerId,cid,fileHash,version);
        }catch (Exception e){
            throw new RuntimeException("BlockChain Registration Failed : " +e.getMessage());
        }
    }
}
