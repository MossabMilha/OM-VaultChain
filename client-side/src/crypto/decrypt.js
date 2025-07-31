import {base64ToArrayBuffer, importRSAPrivateKey, importAESKey} from "./keyUtils.js";

export async function decryptSingleFile(encryptedEnvelope, privateKeyPem){
    try{
        console.log('Encrypted envelope:', encryptedEnvelope); // Debug log
        
        const privateKey = await importRSAPrivateKey(privateKeyPem);
        
        // The server returns 'encryptedKey' field
        const encryptedKeyBuffer = base64ToArrayBuffer(encryptedEnvelope.encryptedKey);
        const AESKeyBuffer = await crypto.subtle.decrypt(
            {name: "RSA-OAEP"},
            privateKey,
            encryptedKeyBuffer
        );
        
        const AESKey = await importAESKey(AESKeyBuffer);
        
        // Server returns 'encryptedData' field  
        const encryptedDataBuffer = base64ToArrayBuffer(encryptedEnvelope.encryptedData);
        const ivBuffer = base64ToArrayBuffer(encryptedEnvelope.iv);

        const decryptedBuffer = await crypto.subtle.decrypt(
            {name: "AES-GCM", iv: new Uint8Array(ivBuffer)},
            AESKey,
            encryptedDataBuffer
        );
        
        const decryptedBlob = new Blob([decryptedBuffer], {type: encryptedEnvelope.mimeType});
        return {
            blob: decryptedBlob,
            fileName: encryptedEnvelope.fileName,
            mimeType: encryptedEnvelope.mimeType,
            fileHash: encryptedEnvelope.fileHash
        };

    }catch (error){
        console.error('Decryption error details:', error); // Better error logging
        throw new Error(`Decryption Failed: ${error.message}`);
    }
}
export async function verifyFileIntegrity(decryptedBlob, expectedHash){
    const hashBuffer = await crypto.subtle.digest("SHA-512", await decryptedBlob.arrayBuffer());
    const computedHash = btoa(String.fromCharCode(...new Uint8Array(hashBuffer)));
    console.log(computedHash);
    return computedHash === expectedHash;
}
export function downloadDecryptedFile(decryptedFile) {
    const url = URL.createObjectURL(decryptedFile.blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = decryptedFile.fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
}
export async function decryptPrivateKey(encryptedPrivateKey, iv, encryptionKey) {
    const decryptedBuffer = await crypto.subtle.decrypt(
        { name: "AES-GCM", iv },
        encryptionKey,
        encryptedPrivateKey
    );
    return new Uint8Array(decryptedBuffer);
}


