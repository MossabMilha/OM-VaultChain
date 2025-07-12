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

    private String fileName;
    private String mimeType;
    private byte[] Data;
    private Map<String,String> recipientPublicKeys;

    public EncryptionRequest(){}
    public EncryptionRequest(byte[] Data,Map<String,String> recipientPublicKeys){
        this.Data = Data;
        this.recipientPublicKeys = recipientPublicKeys;
    }

    public String getFileName(){return this.fileName;}
    public String getMimeType() {return mimeType;}
    public byte[] getData(){return this.Data;}
    public Map<String,String> getRecipientPublicKeys(){return this.recipientPublicKeys;}

    public void setFileName(String fileName){this.fileName = fileName;}
    public void getFileName(String mineType){this.mimeType = mimeType;}
    public void setData(byte[] Data){this.Data = Data;}
    public void setRecipientPublicKeys(Map<String,String> recipientPublicKeys){this.recipientPublicKeys = recipientPublicKeys;}

}
