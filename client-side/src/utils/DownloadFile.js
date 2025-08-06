import {downloadSingleFileApi} from "../services/api/downloadService.js";
import {base64ToArrayBuffer, deriveSharedAESKey,base64ToUint8Array} from "../crypto/keyUtils.js";
import {computeHashFromBuffer} from "../crypto/hash.js";
import {getCurrentUser} from "./userKeyStorage.js";
import {decryptFileData} from "../crypto/decrypt.js";



export async function downloadSingleFile(fileId) {
    try {
        // Validate user session
        const currentUser = getCurrentUser();
        if (!currentUser?.userId) {
            throw new Error("User is not logged in. Please log in to continue.");
        }

        // Fetch file from API
        const response = await downloadSingleFileApi({ fileId, userId: currentUser.userId });
        if (!response || !response.encryptedData) {
            throw new Error("Invalid response received from server while downloading file.");
        }

        const { encryptedData, iv, encryptedKey, fileName, mimeType, fileHash } = response;

        // Derive AES key
        const selfAESKey = await deriveSharedAESKey(
            base64ToUint8Array(currentUser.privateKey),
            base64ToUint8Array(response.ownerPublicKey)
        );

        // Decrypt AES key
        const decryptedAESKeyRaw = await crypto.subtle.decrypt(
            { name: "AES-GCM", iv: base64ToArrayBuffer(iv) },
            selfAESKey,
            base64ToArrayBuffer(encryptedKey)
        );

        // Decrypt file data
        const decryptedFileBuffer = await decryptFileData(
            base64ToArrayBuffer(encryptedData),
            decryptedAESKeyRaw,
            iv
        );

        // Verify file integrity
        const hashData = await computeHashFromBuffer(decryptedFileBuffer);
        if (hashData !== fileHash) {
            throw new Error("File integrity verification failed. The file may be corrupted or tampered with.");
        }

        // Convert buffer to downloadable file
        const blob = new Blob([decryptedFileBuffer], { type: mimeType });
        const fileURL = URL.createObjectURL(blob);

        const a = document.createElement("a");
        a.href = fileURL;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        a.remove();

        URL.revokeObjectURL(fileURL);

        return { success: true, message: "File downloaded successfully." };



    } catch (error) {
        throw new Error(error.message || "Failed to download file. Please try again.");
    }
}






