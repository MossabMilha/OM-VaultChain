package com.omvaultchain.blockchain.service;

import com.omvaultchain.blockchain.contracts.AccessControl;
import com.omvaultchain.blockchain.controller.dto.AccessCheckResponse;
import com.omvaultchain.blockchain.controller.dto.AccessRecord;
import com.omvaultchain.blockchain.controller.dto.GrantMultipleAccessRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.Tuple;
import org.web3j.tuples.generated.Tuple3;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccessRightsService {

    private final SmartContractClient smartContractClient;

    public String grantAccess(String cid,String granteeWallet,String encryptedKey){
        try {
            AccessControl accessControl = smartContractClient.getAccessControl();
            TransactionReceipt receipt = accessControl.grantAccess(cid, granteeWallet, encryptedKey).send();
            return receipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException("Failed to grant access: " + e.getMessage(), e);
        }

    }

    public void revokeAccess(String cid,String walletAddress) {
        smartContractClient.revokeAccessOnChain(cid,walletAddress);
    }

    public AccessCheckResponse hasAccess(String cid, String walletAddress) {
        try{
            var result = smartContractClient.getAccess(cid, walletAddress);
            boolean granted = result.component1();
            BigInteger grantedAtRaw = result.component2();
            String encryptedKey = result.component3();
            return new AccessCheckResponse(
                    cid,
                    walletAddress,
                    granted,
                    grantedAtRaw.equals(BigInteger.ZERO)?null: Instant.ofEpochSecond(grantedAtRaw.longValueExact()),
                    encryptedKey);
        }catch (Exception e){
            throw new RuntimeException("Failed To Fetch Access From Smart Contract ", e);
        }

    }
    public String grantMultipleAccess(String cid, List<Map<String, String>> userId) {
        try{
            AccessControl accessControl = smartContractClient.getAccessControl();
            List<String> users = new ArrayList<>();
            List<String> encryptedKeys = new ArrayList<>();
            for (Map<String, String> user : userId) {
                String userWallet = user.get("walletAddress");
                String encryptedKey = user.get("encryptedKey");
                if (userWallet == null || encryptedKey == null) {
                    throw new IllegalArgumentException(
                            "Each map must contain 'user' and 'encryptedKey' entries");
                }
                users.add(userWallet);
                encryptedKeys.add(encryptedKey);
            }
            TransactionReceipt receipt = accessControl.grantMultipleAccess(cid, users, encryptedKeys).send();
            return receipt.getTransactionHash();
        }catch (Exception e){
            throw new RuntimeException("Failed To Grant Multiple Access: " + e.getMessage(), e);
        }
    }
    public String revokeAllAccess(String cid) {
        try {
            AccessControl accessControl = smartContractClient.getAccessControl();
            TransactionReceipt receipt = accessControl.revokeAllAccess(cid).send();
            return receipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException("Failed to Revoke All Access: " + e.getMessage(), e);
        }
    }

    public List<AccessRecord> getAccessList(String cid) {
        try {
            AccessControl accessControl = smartContractClient.getAccessControl();
            List<String> users = accessControl.getAccessList(cid).send();

            List<AccessRecord> records = new ArrayList<>(users.size());

            for (String user : users) {
                Tuple3<Boolean, BigInteger, String> accessInfo = accessControl.getAccess(cid, user).send();
                Boolean hasAccess = accessInfo.component1();
                BigInteger timestamp = accessInfo.component2();
                String encryptedKey = accessInfo.component3();

                if (Boolean.TRUE.equals(hasAccess)) {
                    records.add(new AccessRecord(user,encryptedKey,Instant.ofEpochSecond(timestamp.longValue()),true));
                }
            }

            return records;

        } catch (Exception e) {
            throw new RuntimeException("Failed To Fetch Access List: " + e.getMessage(), e);
        }
    }


}
