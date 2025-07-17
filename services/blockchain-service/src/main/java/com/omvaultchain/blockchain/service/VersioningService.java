package com.omvaultchain.blockchain.service;

import com.omvaultchain.blockchain.contracts.AccessControl;
import com.omvaultchain.blockchain.contracts.VersionManager;
import com.omvaultchain.blockchain.controller.dto.FileStatusRespond;
import com.omvaultchain.blockchain.controller.dto.VersionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VersioningService {
    @Autowired
    private VersionManager versionManager;
    @Autowired
    private SmartContractClient smartContractClient;

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
    public VersionInfo getCurrentVersion(String fileId) {
        try {

            Tuple3<String, BigInteger, BigInteger> result = versionManager.getCurrentVersion(fileId).send();

            String cid = result.component1();
            BigInteger version = result.component2();
            BigInteger timestamp = result.component3();

            return new VersionInfo(version.longValue(), cid, timestamp.longValue());

        } catch (Exception e) {
            throw new RuntimeException("Failed To Get Current Version : " + e.getMessage());
        }
    }

    public VersionInfo getVersionAt(String fileId, BigInteger versionNumber) {
        try {
            Tuple3<String, BigInteger, BigInteger> result = versionManager.getVersionAt(fileId,versionNumber).send();
            String cid = result.component1();
            BigInteger version = result.component2();
            BigInteger timestamp = result.component3();

            return new VersionInfo(version.longValue(), cid, timestamp.longValue());

        } catch (Exception e) {
            throw new RuntimeException("Failed To Get Version At : " + e.getMessage());
        }
    }

    public FileStatusRespond getFileStatus(String fileId ) {
        try{
            String status = versionManager.getFileStatus(fileId).send();
            return new FileStatusRespond(fileId,status);
        }catch (Exception e){
            throw new RuntimeException("Failed To Get File Status : " + e.getMessage());
        }
    }
    public String deleteFile(String fileId) {
        try {
            TransactionReceipt receipt = versionManager.deleteFile(fileId).send();
            return receipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException("Failed To Delete File : " + e.getMessage());
        }
    }

    public String lockVersion(String fileId,int versionNumber){
        try{
            TransactionReceipt receipt = versionManager.lockVersion(fileId,BigInteger.valueOf(versionNumber)).send();
            return receipt.getTransactionHash();
        }catch (Exception e){
            throw new RuntimeException("Error Locking Version : " + e.getMessage());
        }
    }

    public boolean compareVersions(String fileId,int version1,int version2) {
        try{
            return versionManager.compareVersions(fileId,BigInteger.valueOf(version1),BigInteger.valueOf(version2)).send();
        } catch (Exception e) {
            throw new RuntimeException("Error Comparing Versions : " + e.getMessage());
        }
    }

    public boolean isVersionLocked(String fileId,int versionNumber){
        try {
            return versionManager.isVersionLocked(fileId,BigInteger.valueOf(versionNumber)).send();
        }catch (Exception e){
            throw new RuntimeException("Error Checking If Version Is Lock : " + e.getMessage());
        }
    }




}
