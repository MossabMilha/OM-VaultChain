package com.omvaultchain.blockchain.service;

import com.omvaultchain.blockchain.contracts.AccessControl;
import com.omvaultchain.blockchain.contracts.FileRegistry;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.List;

@Service
public class SmartContractClient {
    @Value("${contract.address.file-registry}")
    private String contractAddress;

    @Value("${contract.address.access-control}")
    private String accessControlAddress;

    @Autowired
    private Web3j web3j;

    @Autowired
    private Credentials credentials;

    private FileRegistry fileRegistry;
    // AccessControl getter
    @Getter
    private AccessControl accessControl;

    @PostConstruct
    public void init() {
        fileRegistry = FileRegistry.load(contractAddress,web3j,credentials,new StaticGasProvider(BigInteger.valueOf(20000000000L), BigInteger.valueOf(6721975)));
        accessControl = AccessControl.load(accessControlAddress, web3j, credentials, new StaticGasProvider(BigInteger.valueOf(20000000000L), BigInteger.valueOf(6721975)));
    }

    // FileRegistry methods
    public String registerFile(String cid, String fileHash)throws Exception{
        TransactionReceipt receipt = fileRegistry.registerFile(cid, fileHash).send();
        return receipt.getTransactionHash();
    }
    public List<FileRegistry.FileRegisteredEventResponse> getFileRegisteredEvents(TransactionReceipt receipt)throws Exception{
        return fileRegistry.getFileRegisteredEvents(receipt);
    }

}
