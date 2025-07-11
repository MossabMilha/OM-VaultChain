package com.omvaultchain.storage.service;

import org.springframework.stereotype.Service;

@Service
public class AccessControlValidator {
    public boolean hasAccess(String walletAddress, String cid){
        // TODO: Integrate with blockchain later
        return true;
    }
}
