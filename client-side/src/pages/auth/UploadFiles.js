
import {getCurrentUser} from "../../utils/userKeyStorage.js";
import {deriveSelfAESKey, arrayBufferToBase64, base64ToUint8Array} from "../../crypto/keyUtils.js";
import {hashFile} from "../../crypto/hash.js";
import {uploadSingleFileApi} from "../../services/api/uploadService.js";

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


        const response = await uploadSingleFileApi(payload);
        console.log("Upload response:", response);


    } catch (error) {
        console.error("File upload failed:", error);
    }
}

document.getElementById('uploadForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    console.log("[Debug] Form submitted");
    const fileInput = document.getElementById('fileInput');
    if (!fileInput.files.length) {
        alert('Please select a file');
        console.warn("[Debug] No file selected");
        return;
    }
    const file = fileInput.files[0];
    console.log("[Debug] Selected file:", file.name, file.size, file.type);
    await uploadSingleFile(file);
});
