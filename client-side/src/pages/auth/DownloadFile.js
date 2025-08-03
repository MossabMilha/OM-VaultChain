import {downloadSingleFileApi} from "../../services/api/downloadService.js";
import {base64ToArrayBuffer, deriveSelfAESKey,base64ToUint8Array} from "../../crypto/keyUtils.js";
import {computeHashFromBuffer} from "../../crypto/hash.js";
import {getCurrentUser} from "../../utils/userKeyStorage.js";
import {decryptFileData} from "../../crypto/decrypt.js";



export async function downloadFile(fileId){
    try {
        const currentUser = getCurrentUser();
        if (!currentUser) throw new Error('No logged-in user found');

        const ownerId = currentUser.userId;
        const response = await downloadSingleFileApi({ fileId, ownerId });

        console.log("Download Response:", response);
        const {encryptedData,iv,encryptedKey,fileName,mimeType,fileHash} = response;
        const selfAESKey = await deriveSelfAESKey(
            base64ToUint8Array(currentUser.privateKey),
            base64ToUint8Array(currentUser.publicKey)
        );
        const decryptedAESKeyRaw = await crypto.subtle.decrypt(
            {name:"AES-GCM",iv:base64ToArrayBuffer(iv)},
            selfAESKey,
            base64ToArrayBuffer(encryptedKey)
        );
        const decryptedFileBuffer = await decryptFileData(
            base64ToArrayBuffer(encryptedData),
            decryptedAESKeyRaw,
            iv
        );

        const hashData = await computeHashFromBuffer(decryptedFileBuffer);
        if (hashData !== fileHash) {
            throw new Error("File integrity check failed! Hash mismatch.");
        }
        // ✅ Convert to Blob and download
        const blob = new Blob([decryptedFileBuffer], { type: mimeType });
        const fileURL = URL.createObjectURL(blob);

        const a = document.createElement("a");
        a.href = fileURL;
        a.download = fileName;
        document.body.appendChild(a); // Needed for Firefox
        a.click();
        a.remove();

        // Cleanup URL object
        URL.revokeObjectURL(fileURL);
        console.log("File decrypted and downloaded successfully!");
    } catch (error) {
        console.error('Download failed:', error);
    }
}

document.getElementById('download').addEventListener('click', async () => {
    await downloadFile("87e69dc1-b45c-454c-bc89-10312f3694a9");
});



