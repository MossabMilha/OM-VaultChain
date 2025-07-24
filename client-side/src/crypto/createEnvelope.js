import {encryptFile,encryptBatchFiles} from "./encrypt.js";
import {wrapAESKeyWithPublicKey} from "./envelopeManager.js";
/**
 * Takes a File and recipient's public key (PEM) and Owner Id,
 * returns:
 * - envelope: JSON metadata (all Base64)
 * - encryptedFile: File object ready for form-data upload
 */
export async function encryptedFileEnvelope(originalFile, recipientPublicKeyPem, ownerId) {
    const encrypted = await encryptFile(originalFile);
    const encryptedKey = await wrapAESKeyWithPublicKey(encrypted.rawAESKeyBase64, recipientPublicKeyPem);

    return {
        encryptedFile: encrypted.encryptedFileBase64,
        metadata: {
            name: originalFile.name,
            size: originalFile.size,
            mimeType: originalFile.type,
            hash: encrypted.fileHashBase64,
            encryptionKey: encryptedKey,
            ownerId: ownerId,
            iv: encrypted.ivBase64,
        }
    };

}
/**
 * Encrypt multiple files
 * Takes Files and recipient's public key (PEM) and Owner Id and return an object:
 * {
 *    ownerId,
 *    files: [ { fileData, fileName, mimeType, iv, encryptedKey, fileHash, sizeBytes }, ... ]
 * }
 */
export async function encryptedFilesEnvelope(originalFiles, recipientPublicKeyPem, ownerId) {
    const encryptedFiles = await encryptBatchFiles(originalFiles);

    const files = await Promise.all(
        encryptedFiles.map(async (encrypted) => {
            const wrappedKey = await wrapAESKeyWithPublicKey(encrypted.encryptedKey, recipientPublicKeyPem);
            return {
                fileName: encrypted.fileName,
                mimeType: encrypted.mimeType,
                sizeBytes: encrypted.sizeBytes,
                iv: encrypted.iv,
                fileHash: encrypted.fileHash,
                encryptedKey: wrappedKey,
                fileData: encrypted.fileData,
            };
        })
    );

    return {
        ownerId,
        files,
    };
}
