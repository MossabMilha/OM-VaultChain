import {encryptFile} from "./encrypt.js";
import {wrapAESKeyWithPublicKey} from "./envelopeManager.js";
/**
 * Takes a File and recipient's public key (PEM) and Owner Id,
 * returns:
 * - envelope: JSON metadata (all Base64)
 * - encryptedFile: File object ready for form-data upload
 */
export async function encryptedFileEnvelope(originalFile, recipientPublicKeyPem,ownerId) {
    const encrypted = await encryptFile(originalFile);
    const encryptedKey = await  wrapAESKeyWithPublicKey(encrypted.rawAESKeyBase64, recipientPublicKeyPem);

    const envelope = {
        fileName: originalFile.name,
        mimeType: originalFile.type,
        ownerId: ownerId,
        sizeBytes: originalFile.size,
        iv: encrypted.ivBase64,
        fileHash: encrypted.fileHashBase64,
        encryptedKey: encryptedKey,
        fileData: encrypted.encryptedFileBase64 // Base64 encoded encrypted file
    };

    return {envelope}
}
/**
 * Encrypt multiple files
 * Takes Files and recipient's public key (PEM) and Owner Id and return an array of envelopes.
 * - envelope: JSON metadata (all Base64)
 * - encryptedFile: File object ready for form-data upload
 */
export async function encryptedFilesEnvelope(originalFiles, recipientPublicKeyPem, ownerId) {
    const envelopes = [];
    for (const file of originalFiles){
        const { envelope } = await encryptedFileEnvelope(file, recipientPublicKeyPem, ownerId);
        envelopes.push(envelope);
    }
    return envelopes;
}