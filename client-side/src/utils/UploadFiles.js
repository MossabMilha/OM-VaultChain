
import {getCurrentUser} from "./userKeyStorage.js";
import {deriveSelfAESKey, arrayBufferToBase64, base64ToUint8Array} from "../crypto/keyUtils.js";
import {hashFile} from "../crypto/hash.js";
import {uploadSingleFileApi} from "../services/api/uploadService.js";

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
        const selfAESKey = await deriveSelfAESKey(base64ToUint8Array(user.privateKey),base64ToUint8Array(user.publicKey))
        const encryptedAESKey = await crypto.subtle.encrypt(
            {name:"AES-GCM",iv},
            selfAESKey,
            rawAESKey
        )
        const payload = {
            encryptedFile: arrayBufferToBase64(encryptedFile),
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


