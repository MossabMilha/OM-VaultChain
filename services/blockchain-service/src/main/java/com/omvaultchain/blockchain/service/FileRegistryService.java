package com.omvaultchain.blockchain.service;

import com.omvaultchain.blockchain.contracts.VersionManager;
import com.omvaultchain.blockchain.controller.dto.FileRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileRegistryService {
    @Autowired
    private SmartContractClient smartContractClient;
    @Autowired
    private VersionManager versionManager;

    public Map<String, Object> registerFileOnBlockChain(FileRegisterRequest request) throws Exception {
        Map<String, Object> receipt = smartContractClient.registerFile(request.getUploaderWallet(), request.getCid(), request.getFileHash(), request.getVersion());
        return receipt;
    }
}