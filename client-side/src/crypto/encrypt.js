import {generateAESKey, exportRawKey, arrayBufferToBase64, base64ToArrayBuffer,importRawKey} from "./keyUtils.js";
import { hashFile } from "./hash.js";

export async function encryptFile(file) {
    const fileBuffer = await file.arrayBuffer();
    const AESKey = await generateAESKey();
    const iv = crypto.getRandomValues(new Uint8Array(12));
    const encryptedData = await crypto.subtle.encrypt({ name: "AES-GCM", iv }, AESKey, fileBuffer);
    const rawAESKeyBuffer = await exportRawKey(AESKey);
    const rawAESKeyBase64 = arrayBufferToBase64(rawAESKeyBuffer);
    const encryptedFileBase64 = arrayBufferToBase64(encryptedData);
    const ivBase64 = arrayBufferToBase64(iv.buffer);
    const fileHashBase64 = await hashFile(file);

    return {
        encryptedFile: encryptedData,
        encryptedFileBase64:encryptedFileBase64,
        ivBase64:ivBase64,
        rawAESKeyBase64:rawAESKeyBase64,
        fileHashBase64:fileHashBase64,
        name: file.name,
        size: file.size,
        mimeType: file.type
    };
}
export async function encryptBatchFiles(files) {
    if (!Array.isArray(files) || files.length === 0) {
        throw new Error("Input must be a non-empty array of files");
    }

    const encryptedFiles = await Promise.all(files.map(file => encryptFile(file)));

    return encryptedFiles.map(encrypted => ({
        fileData: encrypted.encryptedFileBase64,  // rename to fileData for backend
        iv: encrypted.ivBase64,                    // rename ivBase64 → iv
        encryptedKey: encrypted.rawAESKeyBase64,  // rename rawAESKeyBase64 → encryptedKey
        fileHash: encrypted.fileHashBase64,       // rename fileHashBase64 → fileHash
        fileName: encrypted.name,                  // rename name → fileName
        sizeBytes: encrypted.size,                 // rename size → sizeBytes
        mimeType: encrypted.mimeType
    }));


}

export async function decryptFile({ encryptedFileBase64, ivBase64, rawAESKeyBase64 }){
    const encryptedBuffer = base64ToArrayBuffer(encryptedFileBase64);
    const iv = new Uint8Array(base64ToArrayBuffer(ivBase64));
    const rawKey = base64ToArrayBuffer(rawAESKeyBase64);

    const AESKey = await importRawKey(rawKey);
    const decryptedBuffer = await crypto.subtle.decrypt({name:"AES-GCM", iv}, AESKey, encryptedBuffer);
    return new Blob([decryptedBuffer]);

}
