import {getCurrentUser} from "./userKeyStorage.js";
import {getUserPublicInformationApi} from "../services/api/Userinformation.js";
import {getFileMetadata} from "../services/api/FileListingApi.js";
import {arrayBufferToBase64, base64ToArrayBuffer, base64ToUint8Array, deriveSharedAESKey} from "../crypto/keyUtils.js";
import {giveAccessSingleUserApi} from "../services/api/AccessControl.js";



export async function giveAccessSingleUser(userId, FileId) {
    try {


        const currentUser = await getCurrentUser();
        const user = await getUserPublicInformationApi(userId);
        const response  = await getFileMetadata(FileId);
        const fileData = response.data;


        const encryptedKey = fileData.owner_encrypted_key;
        const iv = fileData.iv;
        const userPublicKey = user.publicKey;
        const currentUserPrivateKey = currentUser.privateKey
        const currentUserPublicKey = currentUser.publicKey

        // 1. Derive owner's shared AES key (to decrypt the AES key)
        const selfAESKey  = await deriveSharedAESKey(
            base64ToUint8Array(currentUserPrivateKey),
            base64ToUint8Array(currentUserPublicKey)
        );
        // 2. Decrypt the AES key that owner encrypted for themselves
        const decryptedAESKeyRaw = await crypto.subtle.decrypt(
            { name: "AES-GCM", iv: base64ToArrayBuffer(iv) },
            selfAESKey,
            base64ToArrayBuffer(encryptedKey)
        );
        // 3. Derive shared AES key between owner (private) and recipient (public)
        const sharedAESKeyForUser  = await deriveSharedAESKey(
            base64ToUint8Array(currentUserPrivateKey),
            base64ToUint8Array(userPublicKey)
        );
        // 4. Encrypt the file AES key for recipient using derived shared AES key
        const encryptedAESKeyForUser = await crypto.subtle.encrypt(
            { name: "AES-GCM", iv: base64ToArrayBuffer(iv)},
            sharedAESKeyForUser,
            decryptedAESKeyRaw
        );
        const encryptedKeyBase64 = arrayBufferToBase64(encryptedAESKeyForUser);

        return await giveAccessSingleUserApi(FileId,userId,encryptedKeyBase64);

    } catch (error) {
        console.error("Error fetching access data:", error);
    }
}






