package com.omvaultchain.blockchain.controller;

import com.omvaultchain.blockchain.contracts.FileRegistry;
import com.omvaultchain.blockchain.controller.dto.FileRegisterRequest;
import com.omvaultchain.blockchain.service.FileRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {
    @Autowired
    private FileRegistryService fileRegistryService;

    @PostMapping("/register-file")
    public ResponseEntity<?> registerFile(@RequestBody FileRegisterRequest request ){
        String txHash = fileRegistryService.registerFileOnBlockChain(request.getCid(), request.getFileHash());
        return ResponseEntity.ok(Map.of("transactionHash", txHash));
    }

}
