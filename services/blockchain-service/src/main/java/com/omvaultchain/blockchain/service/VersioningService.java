package com.omvaultchain.blockchain.service;

import com.omvaultchain.blockchain.contracts.VersionManager;
import com.omvaultchain.blockchain.controller.dto.VersionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VersioningService {
    @Autowired
    private VersionManager versionManager;
    public String addVersion(String fileId,String cid){
        try{
            TransactionReceipt receipt =  versionManager.addVersion(fileId,cid).send();
            return receipt.getTransactionHash();
        }catch (Exception e){
            throw new RuntimeException("Failed To Add Version : " +e.getMessage());
        }
    }
    public String rollbackToVersion(String fileId,long versionNumber){
        try{
            TransactionReceipt receipt = versionManager.rollbackToVersion(fileId, BigInteger.valueOf(versionNumber)).send();
            return receipt.getTransactionHash();
        }catch (Exception e){
            throw new RuntimeException("Failed To Rollback Version : " + e.getMessage());
        }
    }

    public List<VersionInfo> getVersionHistory(String fileId) {
        try {
            List<VersionManager.VersionAddedEventResponse> events =
                    versionManager.versionAddedEventFlowable(
                            DefaultBlockParameterName.EARLIEST,
                            DefaultBlockParameterName.LATEST
                    ).toList().blockingGet();

            return events.stream().filter(event -> event.fileId.equals(fileId))
                    .map(
                            event -> new VersionInfo(
                            event.version.longValue(),
                            event.cid,
                            event.timestamp.longValue())
                    ).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch version history: " + e.getMessage());
        }
    }

}
