package com.omvaultchain.storage.service;

import org.springframework.stereotype.Service;

@Service
public class CIDVerifier {
    public void verifyCID(String cid){
        if (cid == null || cid.isBlank()){
            throw new IllegalArgumentException("CID cannot be null or blank");
        }
        if(!cid.matches("^Qm[1-9A-HJ-NP-Za-km-z]{44}$")){
            throw new IllegalArgumentException("Invalid CID format: " + cid);
        }

        // Later: Check against the blockchain for hash integrity

    }
}
