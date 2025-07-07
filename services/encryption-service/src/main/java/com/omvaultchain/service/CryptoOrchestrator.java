package com.omvaultchain.service;

import com.omvaultchain.model.EncryptionResponse;
import com.omvaultchain.model.KeyEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.SecretKey;

import java.security.PublicKey;

import java.util.Map;

public class CryptoOrchestrator {
    @Autowired
    public AESService AES_Service;

    @Autowired
    public   AsymmetricEncryptionService AE_Service;

    @Autowired
    public FileHashService FileHash_Service;

    @Autowired
    public IVGenerator IV_Generator;

    @Autowired
    public KeyEnvelopeBuilder KeyEnvelope_Service;


    public EncryptionResponse encryptFile(byte[] Data, Map<String, PublicKey> recipientPublicKeys) throws Exception {
        // 1. Generate AES key + IV
        SecretKey AES_Key = AES_Service.generateKey();
        byte[] IV = IVGenerator.generateIV();

        // 2. Encrypt file with AES
        byte[] encryptedData = AES_Service.encrypt(Data,AES_Key,IV);

        // 3. Generate file hash (original or encrypted)
        String DataHash = FileHash_Service.computeSHA256(Data);

        // 4. Encrypt AES key for each recipient
        KeyEnvelope keyEnvelope = KeyEnvelope_Service.buildEnvelope(AES_Key.getEncoded(), recipientPublicKeys);


        return new EncryptionResponse(DataHash,encryptedData, IV, keyEnvelope.getEncryptedKeys());

    }

}
