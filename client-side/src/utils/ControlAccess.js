import {getCurrentUser} from "./userKeyStorage.js";
import {getUserPublicInformationApi} from "../services/api/Userinformation.js";
import {getFileMetadata} from "../services/api/FileListingApi.js";
import {arrayBufferToBase64, base64ToArrayBuffer, base64ToUint8Array, deriveSharedAESKey} from "../crypto/keyUtils.js";
import {giveAccessSingleUserApi} from "../services/api/AccessControl.js";



async function giveAccessSingleUser(userId, FileId) {
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

        const respond = await giveAccessSingleUserApi(FileId,userId,encryptedKeyBase64);

        console.log(respond);

    } catch (error) {
        console.error("Error fetching access data:", error);
    }
}

document.getElementById("test").addEventListener("click", async ()=>{
    await giveAccessSingleUser("51cf531f-8bc4-44a9-9bfd-1183cab10d47","1301400b-1a4c-4f9a-8bbd-5c29c6df8f33");
});




