import {base64ToArrayBuffer} from "./keyUtils.js";


export async function decryptFileData(encryptedFile,rawAESKey,iv){
    const AESKey = await  crypto.subtle.importKey(
        "raw",
        rawAESKey,
        {name:"AES-GCM"},
        false,
        ["decrypt"]
    );
    return await crypto.subtle.decrypt(
        {name:"AES-GCM",iv:base64ToArrayBuffer(iv)},
        AESKey,
        encryptedFile
    );

}
export async function verifyFileIntegrity(decryptedBlob, expectedHash){
    const hashBuffer = await crypto.subtle.digest("SHA-512", await decryptedBlob.arrayBuffer());
    const computedHash = btoa(String.fromCharCode(...new Uint8Array(hashBuffer)));
    console.log(computedHash);
    return computedHash === expectedHash;
}

export async function decryptPrivateKey(encryptedPrivateKey, iv, encryptionKey) {
    const decryptedBuffer = await crypto.subtle.decrypt(
        { name: "AES-GCM", iv },
        encryptionKey,
        encryptedPrivateKey
    );
    return new Uint8Array(decryptedBuffer);
}


