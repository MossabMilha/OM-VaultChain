package com.omvaultchain.service;

import java.security.SecureRandom;
/**
 * IVGenerator
 *
 * This utility class is responsible for generating cryptographically secure Initialization Vectors (IVs)
 * for AES-GCM encryption, as required by modern encryption standards.
 *
 * Usage Context:
 * - When a file is encrypted using AES-256-GCM:
 *   → A new random 12-byte IV must be generated for each encryption operation.
 *   → The IV is used along with the AES key to perform authenticated encryption.
 *   → The IV is later needed to decrypt the ciphertext and must be stored or transmitted securely.
 *
 * Why 12 Bytes?
 * - AES-GCM specifically recommends a 96-bit (12-byte) IV for optimal security and performance.
 * - Using random IVs prevents replay and chosen-plaintext attacks.
 *
 * Security Notes:
 * - A new IV must be used every time encryption is performed with the same key.
 * - IVs do not need to be secret, but **must never be reused** with the same key.
 * - SecureRandom ensures strong randomness suitable for cryptographic purposes.
 */
 public class IVGenerator {
    private static final int IV_SIZE = 12; // GCM recommended

    public static byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
