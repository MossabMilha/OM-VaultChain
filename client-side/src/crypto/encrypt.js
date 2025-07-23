import {generateAESKey, exportRawKey, arrayBufferToBase64} from "./keyUtils.js"
import {hashFile} from "./hash.js"


export async function encryptFile(file) {
    const fileBuffer = await file.arrayBuffer();
    const AESKey = await generateAESKey();
    const iv = crypto.getRandomValues(new Uint8Array(12));
    const encryptedData = await crypto.subtle.encrypt({name:"AES-GCM",iv},AESKey,fileBuffer);
    const rawAESKey = await exportRawKey(AESKey);
    const fileHash = await hashFile(file);
    return {
        encryptedFile : encryptedData,
        iv : iv,
        rawKey : rawAESKey,
        fileHash : fileHash,
        name : file.name,
        size : file.size,
        mimeType : file.type
    }
}
