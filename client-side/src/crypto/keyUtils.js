import {getPublicKey, getSharedSecret} from "@noble/secp256k1";
import {keccak_256} from "@noble/hashes/sha3";


export async function generateAESKey() {
    return crypto.subtle.generateKey(
        {name: "AES-GCM",length: 256},
        true,
        ["encrypt", "decrypt"]
    );
}
export function arrayBufferToBase64(buffer) {
    try {
        if (!buffer) {
            throw new Error('Buffer is null or undefined');
        }

        const bytes = new Uint8Array(buffer);
        let binary = '';
        for(let b of bytes) binary += String.fromCharCode(b);
        return btoa(binary);
    } catch (error) {
        throw new Error(`Failed to convert ArrayBuffer to base64: ${error.message}`);
    }
}
export function base64ToArrayBuffer(base64) {
    try {
        // Validate base64 string
        if (!base64 || typeof base64 !== 'string') {
            throw new Error('Invalid base64 input: must be a non-empty string');
        }

        // Clean the base64 string (remove any whitespace)
        const cleanBase64 = base64.replace(/\s/g, '');

        // Validate base64 format
        if (!/^[A-Za-z0-9+/]*={0,2}$/.test(cleanBase64)) {
            throw new Error('Invalid base64 format: contains invalid characters');
        }

        const binary = atob(cleanBase64);
        const bytes = new Uint8Array(binary.length);
        for (let i = 0; i < binary.length; i++) {
            bytes[i] = binary.charCodeAt(i);
        }
        return bytes.buffer;
    } catch (error) {

        throw new Error(`Failed to decode base64: ${error.message}`);
    }
}
export function base64ToUint8Array(base64) {
    return Uint8Array.from(atob(base64), c => c.charCodeAt(0));
}
export async function importRSAPublicKey(base64key){
    const keyBuffer = base64ToArrayBuffer(base64key);
    return await crypto.subtle.importKey(
        "spki",           // format of public key
        keyBuffer,
        {
            name: "RSA-OAEP",
            hash: "SHA-256",
        },
        true,             // extractable (true if you want to export later)
        ["encrypt"]       // key usages
    );
}
export async function importAESKey(rawKeyBuffer){
    return crypto.subtle.importKey(
        "raw",
        rawKeyBuffer,
        { name: "AES-GCM" },
        true,
        ["encrypt", "decrypt"]);
}
export async function exportRawKey(key) {
    return crypto.subtle.exportKey("raw",key);
}

export async function generateKeyPair() {
    const privateKey = crypto.getRandomValues(new Uint8Array(32)); // 32-byte private key
    const publicKey = getPublicKey(privateKey, true); // Compressed public key
    return { privateKey, publicKey };
}
export function deriveWalletAddress(publicKey) {
    const addressHash = keccak_256(publicKey.slice(1)); // Skip 0x04 prefix if uncompressed
    return '0x' + Array.from(addressHash.slice(-20))
        .map(b => b.toString(16).padStart(2, '0'))
        .join('');
}
export async function deriveSharedAESKey(privateKey,publicKey ){
    const sharedSecret = getSharedSecret(privateKey,publicKey,true);
    return await crypto.subtle.importKey(
        "raw",
        sharedSecret.slice(1),
        {name:"AES-GCM"},
        false,
        ["encrypt","decrypt"]

    )
}






