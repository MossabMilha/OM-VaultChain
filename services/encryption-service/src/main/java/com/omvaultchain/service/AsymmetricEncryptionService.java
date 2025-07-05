package com.omvaultchain.service;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
/**
 * AsymmetricEncryptionService
 * This service handles RSA-based asymmetric encryption used to securely wrap and unwrap AES keys.
 * In real-world usage:
 * - When a user uploads a file:
 *   → A random AES-256 key is generated to encrypt the file content.
 *   → That AES key is then encrypted (wrapped) with the recipient's RSA public key
 *     using OAEP padding and stored along with the encrypted file.
 * - When the user later downloads the file:
 *   → The encrypted AES key is retrieved and decrypted (unwrapped) on the client side
 *     using the user's private RSA key.
 * Notes:
 * - Public keys are stored server-side (e.g., via auth-service) and used to encrypt.
 * - Private keys must remain on the client side and are never transmitted to the backend.
 * - This approach ensures that only the intended user can access the file's AES key
 *   and decrypt the file locally, preserving end-to-end confidentiality.
 */

public class AsymmetricEncryptionService {

    public byte[] wrapAESKey(byte[] aesKey, PublicKey recipientPublicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,recipientPublicKey);
        return cipher.doFinal(aesKey);
    }

    public byte[] unwrapAESKey(byte[] wrappedKey, PrivateKey privateKey)throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        return cipher.doFinal(wrappedKey);

    }
}
