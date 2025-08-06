
import {getCurrentUser} from "./userKeyStorage.js";
import {arrayBufferToBase64, base64ToUint8Array, deriveSharedAESKey} from "../crypto/keyUtils.js";
import {hashFile} from "../crypto/hash.js";
import {uploadBatchFileApi, uploadSingleFileApi} from "../services/api/uploadService.js";
import {derive} from "ecies-geth";

export async function uploadSingleFile(file){
    try {

        const user = getCurrentUser();

        if(!user || !user.publicKey) throw new Error ("Missing User Credential");
        const AESKey = await crypto.subtle.generateKey(
            {name:"AES-GCM",length:256},
            true,
            ["encrypt", "decrypt"]
        )
        const fileBuffer = await file.arrayBuffer();
        const iv = crypto.getRandomValues(new Uint8Array(12));
        const encryptedFile = await crypto.subtle.encrypt(
            {name : "AES-GCM",iv},
            AESKey,
            fileBuffer
        );
        const rawAESKey = await crypto.subtle.exportKey("raw",AESKey);
        const selfAESKey = await deriveSharedAESKey(base64ToUint8Array(user.privateKey),base64ToUint8Array(user.publicKey))
        const encryptedAESKey = await crypto.subtle.encrypt(
            {name:"AES-GCM",iv},
            selfAESKey,
            rawAESKey
        )
        const payload = {
            encryptedFile: arrayBufferToBase64(encryptedFile),
            uploaderWallet: user.walletAddress,
            metadata: {
                ownerId: user.userId,
                name: file.name,
                size: file.size,
                mimeType: file.type,
                iv: arrayBufferToBase64(iv),
                encryptionKey: arrayBufferToBase64(encryptedAESKey),
                hash: await hashFile(file)
            }
        };
        return await uploadSingleFileApi(payload);

    } catch (error) {
        console.error("File upload failed:", error);
    }
}
export async function uploadBatchFile(files){
    try{
        const user = getCurrentUser();
        if(!user || !user.publicKey || !user.privateKey) throw new Error ("Missing User Credential");
         const selfAESKey = await deriveSharedAESKey(
             base64ToUint8Array(user.privateKey),
             base64ToUint8Array(user.publicKey)
         );
         const batchFilesPayload = [];
         for (const file of files){
             const AESKey = await crypto.subtle.generateKey(
                 {name:"AES-GCM",length:256},
                 true,
                 ["encrypt", "decrypt"]
             );
             const fileBuffer = await file.arrayBuffer();
             const iv = crypto.getRandomValues(new Uint8Array(12));
             const encryptedFile = await crypto.subtle.encrypt(
                 {name:"AES-GCM",iv},
                 AESKey,
                 fileBuffer
             );
             const rawAESKey = await crypto.subtle.exportKey("raw",AESKey);
             const encryptedAESKey = await crypto.subtle.encrypt(
                 {name:"AES-GCM",iv},
                 selfAESKey,
                 rawAESKey
             );
             batchFilesPayload.push({
                 fileName:file.name,
                 mimeType: file.type,
                 sizeBytes: file.size,
                 iv: arrayBufferToBase64(iv),
                 fileHash: await hashFile(file),
                 encryptedKey: arrayBufferToBase64(encryptedAESKey),
                 fileData: arrayBufferToBase64(encryptedFile)
             });
         }
         const payload = {
             ownerId: user.userId,
             files: batchFilesPayload
         }
        return await uploadBatchFileApi(payload);
    }catch (error) {
        console.error("Batch file upload failed:", error);
        throw error;
    }
}





