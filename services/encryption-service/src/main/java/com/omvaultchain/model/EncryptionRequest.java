package com.omvaultchain.model;

import java.util.Map;
/**
 * EncryptionRequest
 *
 * Represents the input payload sent to the encryption microservice.
 * It contains the raw file data and the recipient public keys needed
 * to perform AES encryption and RSA key wrapping.
 *
 * Usage:
 * - The client sends this object in a POST request to the /encrypt endpoint.
 *
 * Fields:
 * - data: Raw byte array of the file to encrypt (usually Base64-encoded in JSON).
 * - recipientPublicKeys: A map of recipient identifiers (e.g. wallet addresses)
 *   to their RSA public keys encoded in Base64.
 *
 * Example:
 * {
 *   "data": "<Base64EncodedFileBytes>",
 *   "recipientPublicKeys": {
 *     "0xabc123...": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A...",
 *     "0xdef456...": "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8A..."
 *   }
 * }
 */

public class EncryptionRequest {
    private byte[] Data;
    private Map<String,String> recipientPublicKeys;

    public EncryptionRequest(){}
    public EncryptionRequest(byte[] Data,Map<String,String> recipientPublicKeys){
        this.Data = Data;
        this.recipientPublicKeys = recipientPublicKeys;
    }

    public byte[] getData(){return this.Data;}
    public Map<String,String> getRecipientPublicKeys(){return this.recipientPublicKeys;}

    public void setData(byte[] Data){this.Data = Data;}
    public void setRecipientPublicKeys(Map<String,String> recipientPublicKeys){this.recipientPublicKeys = recipientPublicKeys;}

}
