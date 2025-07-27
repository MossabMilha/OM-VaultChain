import {generateAESKey, exportRawKey, arrayBufferToBase64} from "./keyUtils.js";
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
        fileData: encrypted.encryptedFileBase64,
        iv: encrypted.ivBase64,
        encryptedKey: encrypted.rawAESKeyBase64,
        fileHash: encrypted.fileHashBase64,
        fileName: encrypted.name,
        sizeBytes: encrypted.size,
        mimeType: encrypted.mimeType
    }));


}

