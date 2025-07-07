package com.omvaultchain.service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AESService
 *
 * Provides AES-256-GCM encryption and decryption operations.
 * Requires a SecretKey and an Initialization Vector (IV) provided from outside.
 *
 * - AES-256-GCM encryption/decryption
 * - Tag verification
 *
 * Key generation and IV management are handled by separate services.
 */


public class AESService {
    private final int KEY_SIZE = 256;
    private final int T_LEN = 128;
    private Cipher encryptionCipher;

    /**
     * Generate a new random AEV Key(256 bites).
     */
    public SecretKey  generateKey() throws Exception // 256-bit key
    {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        return generator.generateKey();
    }

    /**
     * Encrypt the data using AES-256-GCM.
     */
    public byte[] encrypt(byte[] data, SecretKey key, byte[] iv) throws Exception {
        encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, iv);
        encryptionCipher.init(Cipher.ENCRYPT_MODE,key,spec);
        return encryptionCipher.doFinal(data);
    }
    /**
     * Decrypt the data using AES-256-GCM.
     */
    public byte[] decrypt(byte[] encryptedData, SecretKey key, byte[] iv) throws Exception {
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN,iv);
        decryptionCipher.init(Cipher.DECRYPT_MODE, key,spec);
        return decryptionCipher.doFinal(encryptedData);
    }
}
