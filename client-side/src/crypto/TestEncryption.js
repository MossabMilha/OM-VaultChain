import fs from 'fs/promises';
import path from 'path';
import { encryptFile } from './encrypt.js';
import {wrapAESKeyWithPublicKey} from "./envelopeManager.js";


async function createBrowserFile(FilePath) {
    const buffer = await fs.readFile(FilePath);
    return {
        name: path.basename(FilePath),
        size: buffer.length,
        type: 'application/octet-stream',
        arrayBuffer: async () =>
            buffer.buffer.slice(buffer.byteOffset, buffer.byteOffset + buffer.byteLength),
    };
}

async function run() {
    const filePath = "C:\\Users\\PC\\Downloads\\TECH EXPERIENCE 7th SPONSORSHIP PROPOSAL ENG.pdf";
    const PublicKeyPem = `-----BEGIN PUBLIC KEY-----
    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvvRnANKhiaPPizdd8zkPnUPQabEvVOUgAn/pgDSVxkJRTmk0Snn6wFfgWYC7nlhwBZB6V1tJ4lA2Tgjhulzol321MvoQQTR+c4xIYtqrWhbG6rhiJwK5sk+RMNvshGtC0exRKqjfT/CGToSuP7nVQ8wL5cAB/tv7n2BWAH/PL1H2A0uwcn3Sn0xbjcmNIhQm99dOSn7xzljrX8vuuc3omNSYgWZCyBM0o9pGzCDNJ5QnwnxZL0PHRJ+H/LzDJ6FvbMYIZBgaP7LNFVPQBY5jrCVnDCD18XodTOTz7TjudQFalz6lpCeVtvwu9MfH3cNgYLJ/uZVlN1UIv7PBMV8EGQIDAQAB
-----END PUBLIC KEY-----`;
    try {
        const file = await createBrowserFile(filePath);
        const { encryptedFileBase64, ivBase64, rawKeyBase64 ,fileHashBase64,name,size,mimeType} = await encryptFile(file);
        const wrappedKeyBase64 = await wrapAESKeyWithPublicKey(rawKeyBase64,PublicKeyPem);
        
        // console.log("Encrypted File Base64:", encryptedFileBase64);
        console.log("Raw Key Base64:", rawKeyBase64);
        console.log("Wrapped Key Base64:", wrappedKeyBase64);
        console.log("IV Base64:", ivBase64);
        console.log("File Hash Base64:", fileHashBase64);
        console.log("File Name:", name);
        console.log("File Size:", size);
        console.log("File MIME Type:", mimeType);
        // console.log("Public Key:", PublicKeyPem);

    } catch (error) {
        console.error("Error during encryption:", error);
    }


}

run();