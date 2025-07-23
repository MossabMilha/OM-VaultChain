import { generateAESKey, exportRawKey, arrayBufferToBase64 } from "./keyUtils.js";
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
