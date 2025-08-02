import {generateAESKey, exportRawKey, arrayBufferToBase64} from "./keyUtils.js";
import {Buffer} from "buffer";
import {hashFile } from "./hash.js";
import {getSharedSecret } from "@noble/secp256k1";
import {base64ToUint8Array} from "./keyUtils.js";

export async function encryptSingleFile(file,PublicKey){
    const fileBuffer = await file.arrayBuffer();
    const AESKey = await generateAESKey();
    const iv = crypto.getRandomValues(new Uint8Array(12));
    const encryptedData = await crypto.subtle.encrypt({ name: "AES-GCM", iv }, AESKey, fileBuffer);
    const rawAESKeyBuffer = await exportRawKey(AESKey);
    const wrappedAESKey = await crypto.subtle.encrypt({name:"RSA-OAEP"},PublicKey,rawAESKeyBuffer);
    const fileHash  = await hashFile(file);

    return {
        encryptedFile: encryptedData,
        encryptedFileBase64:arrayBufferToBase64(encryptedData),
        encryptedAESKeyBase64: arrayBufferToBase64(wrappedAESKey),
        ivBase64: arrayBufferToBase64(iv.buffer),
        fileHashBase64: fileHash,
        name: file.name,
        size: file.size,
        mimeType: file.type
    }
}

// export async function encryptBatchFiles(files) {
//     if (!Array.isArray(files) || files.length === 0) {
//         throw new Error("Input must be a non-empty array of files");
//     }
//
//     const encryptedFiles = await Promise.all(files.map(file => encryptFile(file)));
//
//     return encryptedFiles.map(encrypted => ({
//         fileData: encrypted.encryptedFileBase64,
//         iv: encrypted.ivBase64,
//         encryptedKey: encrypted.rawAESKeyBase64,
//         fileHash: encrypted.fileHashBase64,
//         fileName: encrypted.name,
//         sizeBytes: encrypted.size,
//         mimeType: encrypted.mimeType
//     }));
//
//
// }
export async function encryptPrivateKey(privateKey,encryptionKey){
    const iv = crypto.getRandomValues(new Uint8Array(12));
    const encryptedPrivateKey = await crypto.subtle.encrypt(
        {name:"AES-GCM",iv},
        encryptionKey,
        privateKey

    );
    return {encryptedPrivateKey,iv}
}



async function encryptMessage(receiverPublicKey, senderPrivateKey, message) {
    // 1. Derive shared secret (ECDH)
    const sharedSecret = getSharedSecret(senderPrivateKey, receiverPublicKey);

    // 2. Import shared secret as AES-GCM key
    const aesKey = await crypto.subtle.importKey(
        "raw",
        sharedSecret.slice(1), // Remove first byte (formatting) if needed
        "AES-GCM",
        false,
        ["encrypt"]
    );

    // 3. Encrypt message
    const iv = crypto.getRandomValues(new Uint8Array(12));
    const encodedMessage = new TextEncoder().encode(message);
    const ciphertext = await crypto.subtle.encrypt({ name: "AES-GCM", iv }, aesKey, encodedMessage);

    return { iv, ciphertext };
}
async function decryptMessage(senderPublicKey, receiverPrivateKey, iv, ciphertext) {
    // 1. Derive shared secret (ECDH)
    const sharedSecret = getSharedSecret(receiverPrivateKey, senderPublicKey);

    // 2. Import shared secret as AES-GCM key
    const aesKey = await crypto.subtle.importKey(
        "raw",
        sharedSecret.slice(1),
        "AES-GCM",
        false,
        ["decrypt"]
    );

    // 3. Decrypt
    const decryptedBuffer = await crypto.subtle.decrypt({ name: "AES-GCM", iv }, aesKey, ciphertext);
    return new TextDecoder().decode(decryptedBuffer);
}
export async function testEncryptDecrypt(publicKeyBase64, privateKeyBase64) {
    try {
        const publicKey = base64ToUint8Array(publicKeyBase64);
        const privateKey = base64ToUint8Array(privateKeyBase64);

        const message = "Hello OM VaultChain!";

        const { iv, ciphertext } = await encryptMessage(publicKey, privateKey, message);
        const decryptedText = await decryptMessage(publicKey, privateKey, iv, ciphertext);


        return decryptedText === message;
    } catch (error) {
        console.error("❌ ECIES test failed:", error.message);
        return false;
    }
}
