package com.omvaultchain.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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
@Service
public class AsymmetricEncryptionService {

    public  byte[] wrapAESKey(byte[] aesKey, PublicKey recipientPublicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,recipientPublicKey);
        return cipher.doFinal(aesKey);
    }
    public byte[] wrapAESKey(SecretKey aesKey, PublicKey recipientPublicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, recipientPublicKey);
        return cipher.doFinal(aesKey.getEncoded());
    }
    // 3. Accept SecretKey and String base64-encoded public key
    public byte[] wrapAESKey(SecretKey aesKey, String base64RecipientPublicKey) throws Exception {
        PublicKey publicKey = loadPublicKeyFromBase64(base64RecipientPublicKey);
        return wrapAESKey(aesKey, publicKey);
    }


    public PublicKey loadPublicKeyFromBase64(String base64) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(base64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public  byte[] unwrapAESKey(byte[] wrappedKey, PrivateKey privateKey)throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        return cipher.doFinal(wrappedKey);

    }
}
