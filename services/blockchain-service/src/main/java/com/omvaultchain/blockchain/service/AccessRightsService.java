package com.omvaultchain.blockchain.service;

import com.omvaultchain.blockchain.contracts.AccessControl;
import com.omvaultchain.blockchain.controller.dto.AccessCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.Tuple;
import org.web3j.tuples.generated.Tuple3;

import java.math.BigInteger;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AccessRightsService {

    private final SmartContractClient contractClient;
    private final SmartContractClient smartContractClient;

    public String grantAccess(String cid,String granteeWallet,String encryptedKey) throws Exception{
        AccessControl accessControl = contractClient.getAccessControl();
        TransactionReceipt receipt = accessControl.grantAccess(cid, granteeWallet, encryptedKey).send();
        return receipt.getTransactionHash();
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


}
