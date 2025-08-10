
import {getCurrentUser} from "./userKeyStorage.js";
import {arrayBufferToBase64, base64ToUint8Array, deriveSharedAESKey} from "../crypto/keyUtils.js";
import {hashFile} from "../crypto/hash.js";
import {uploadBatchFileApi, uploadNewVersionApi, uploadSingleFileApi} from "../services/api/uploadService.js";
import {getUserWithAccessInfoApi} from "../services/api/Userinformation.js";

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

export async function uploadNewVersion(file, fileId) {
    try {
        const currentUser = await getCurrentUser();
        if (!currentUser || !currentUser.publicKey || !currentUser.privateKey) {
            throw new Error("Missing User Credential");
        }

        // Get all users who have access to the old version (excluding owner)
        const res  = await getUserWithAccessInfoApi(currentUser.userId, fileId);
        const usersWithAccess = Array.isArray(res) ? res : (res?.users || []);


        // 1. Generate AES key for the new file version
        const AESKey = await crypto.subtle.generateKey(
            { name: "AES-GCM", length: 256 },
            true,
            ["encrypt", "decrypt"]
        );

        // 2. Encrypt the file with the AES key
        const fileBuffer = await file.arrayBuffer();
        const iv = crypto.getRandomValues(new Uint8Array(12));
        const encryptedFile = await crypto.subtle.encrypt(
            { name: "AES-GCM", iv },
            AESKey,
            fileBuffer
        );

        // 3. Export raw AES key
        const rawAESKey = await crypto.subtle.exportKey("raw", AESKey);

        // 4. Encrypt AES key for the owner
        const selfSharedKey = await deriveSharedAESKey(
            base64ToUint8Array(currentUser.privateKey),
            base64ToUint8Array(currentUser.publicKey)
        );
        const encryptedAESKeyForOwner = await crypto.subtle.encrypt(
            { name: "AES-GCM", iv },
            selfSharedKey,
            rawAESKey
        );

        // 5. Encrypt AES key for each user with access
        console.log(usersWithAccess);
        const encryptedKeysForUsers = {};
        for (const u of usersWithAccess) {
            const sharedKey = await deriveSharedAESKey(
                base64ToUint8Array(currentUser.privateKey),
                base64ToUint8Array(u.public_key)
            );
            const encryptedKey = await crypto.subtle.encrypt(
                { name: "AES-GCM", iv },
                sharedKey,
                rawAESKey
            );
            encryptedKeysForUsers[u.id] = arrayBufferToBase64(encryptedKey);
        }

        // 6. Build payload
        const payload = {
            fileId: fileId,
            encryptedFile: arrayBufferToBase64(encryptedFile),
            metadata: {
                ownerId: currentUser.userId,
                name: file.name,
                size: file.size,
                mimeType: file.type,
                iv: arrayBufferToBase64(iv),
                encryptionKey: arrayBufferToBase64(encryptedAESKeyForOwner),
                hash: await hashFile(file)
            },
            encryptionKeys: encryptedKeysForUsers
        };

        // 7. Upload new version
        return await uploadNewVersionApi(payload);

    } catch (error) {
        console.error("File upload failed:", error);
    }
}




