package com.omvaultchain.service;

import java.security.SecureRandom;

public class IVGenerator {
    private static final int IV_SIZE = 12; // GCM recommended

    public static byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
