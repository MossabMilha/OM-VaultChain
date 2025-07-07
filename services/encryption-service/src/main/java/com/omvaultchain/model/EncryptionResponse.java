package com.omvaultchain.model;

import java.util.Map;
/**
 * EncryptionResponse
 *
 * Represents the result of the file encryption process returned by the encryption microservice.
 * It includes the AES-encrypted file, the IV used during encryption, the encrypted AES keys for
 * each recipient, and the original file hash.
 *
 * Usage:
 * - Returned by the /encrypt endpoint as a JSON response.
 * - Allows the client to store the encrypted file, then decrypt it later using the IV and
 *   the correct wrapped key.
 *
 * Fields:
 * - hashedData: The SHA-256 hash of the original file (hex-encoded string).
 * - encryptedData: AES-256-GCM encrypted file content (as a byte array).
 * - iv: 12-byte Initialization Vector used in AES-GCM (needed for decryption).
 * - encryptedKeys: A map of recipient IDs to their RSA-encrypted AES keys.
 *
 * Example (JSON output):
 * {
 *   "hashedData": "9c56cc51edb4f2c17bdf29f2af4e909d...",
 *   "encryptedData": "<Base64EncryptedFile>",
 *   "iv": "<Base64IV>",
 *   "encryptedKeys": {
 *     "0xabc123...": "<Base64WrappedAESKey>"
 *   }
 * }
 */
public class EncryptionResponse {
    private String HashedData;
    private byte[] EncryptedData;
    private byte[] IV;
    private Map<String, byte[]> EncryptedKeys;

    public EncryptionResponse() {}

    public EncryptionResponse(String fileHash, byte[] encryptedFileData, byte[] iv, Map<String, byte[]> encryptedKeys) {
        this.HashedData = fileHash;
        this.EncryptedData = encryptedFileData;
        this.IV = iv;
        this.EncryptedKeys = encryptedKeys;
    }
    public String getHashedData(){return this.HashedData;}
    public byte[] getEncryptedData(){return this.EncryptedData;}
    public byte[] getIV(){return this.IV;}
    public Map<String,byte[]> getEncryptedKeys(){return this.EncryptedKeys;}

    public void getHashedData(String HashedData){this.HashedData = HashedData;}
    public void getEncryptedData(byte[] EncryptedData){this.EncryptedData = EncryptedData;}
    public void getIV(byte[] IV){this.IV = IV;}
    public void getEncryptedKeys(Map<String,byte[]> EncryptedKeys){this.EncryptedKeys = EncryptedKeys;}


}
