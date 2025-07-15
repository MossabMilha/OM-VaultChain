package com.omvaultchain.blockchain.service;

import com.omvaultchain.blockchain.contracts.AccessControl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Service
@RequiredArgsConstructor
public class AccessRightsService {

    private final SmartContractClient contractClient;

    public String grantAccess(String cid,String granteeWallet,String encryptedKey) throws Exception{
        AccessControl accessControl = contractClient.getAccessControl();
        TransactionReceipt receipt = accessControl.grantAccess(cid, granteeWallet, encryptedKey).send();
        return receipt.getTransactionHash();
    }


}
