
import { getPublicKey } from "@noble/secp256k1";
import { keccak_256 } from "@noble/hashes/sha3";



export function pemToBase64(pem) {
    return pem
        .replace(/-----BEGIN (?:RSA )?(?:PUBLIC|PRIVATE) KEY-----/, '')
        .replace(/-----END (?:RSA )?(?:PUBLIC|PRIVATE) KEY-----/, '')
        .replace(/\s+/g, '');
}



export async function generateAESKey() {
    return crypto.subtle.generateKey({name: "AES-GCM",length: 256},true, ["encrypt", "decrypt"]);
}

export function arrayBufferToBase64(buffer) {
    try {
        if (!buffer) {
            throw new Error('Buffer is null or undefined');
        }

        const bytes = new Uint8Array(buffer);
        let binary = '';
        for(let b of bytes) binary += String.fromCharCode(b);
        const result = btoa(binary);
        return result;
    } catch (error) {
        throw new Error(`Failed to convert ArrayBuffer to base64: ${error.message}`);
    }
}
export function uint8ArrayToBase64(uint8Array) {
    let binary = "";
    for (let i = 0; i < uint8Array.byteLength; i++) {
        binary += String.fromCharCode(uint8Array[i]);
    }
    return btoa(binary);
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

export async function strToUint8Array(str) {
    // assuming signature is hex string starting with '0x'
    if (str.startsWith("0x")) str = str.slice(2);
    const bytes = new Uint8Array(str.length / 2);
    for (let i = 0; i < bytes.length; i++) {
        bytes[i] = parseInt(str.substr(i * 2, 2), 16);
    }
    return bytes;
}

export async function importRawKey(RawKeyBuffer) {
    return await crypto.subtle.importKey("raw",RawKeyBuffer,"AES-GCM",true,["decrypt"]);

}
export async function importRSAPrivateKey(privateKeyPem) {
    try {
        const cleanPrivateKey = pemToBase64(privateKeyPem);
        const keyBuffer = base64ToArrayBuffer(cleanPrivateKey);

        return await crypto.subtle.importKey(
            'pkcs8',
            keyBuffer,
            {
                name: 'RSA-OAEP',
                hash: 'SHA-512'
            },
            false,
            ['decrypt']
        );
    } catch (error) {
        throw new Error(`Failed to import RSA private key: ${error.message}`);
    }
}
export async function importRSAPublicKey(publicKeyPem) {
    try {
        const cleanPublicKey = pemToBase64(publicKeyPem);
        const keyBuffer = base64ToArrayBuffer(cleanPublicKey);

        return await crypto.subtle.importKey(
            'spki',
            keyBuffer,
            {
                name: 'RSA-OAEP',
                hash: 'SHA-512'
            },
            true,
            ['encrypt']
        );
    } catch (error) {
        throw new Error(`Failed to import RSA public key: ${error.message}`);
    }
}
export async function importAESKey(keyBuffer){
    return await crypto.subtle.importKey(
        "raw",
        keyBuffer,
        {name: "AES-GCM"},
        false,
        ["decrypt"]
    );
}

export async function exportRawKey(cryptoKey) {
    try {
        const rawKeyBuffer = await crypto.subtle.exportKey("raw", cryptoKey);
        return rawKeyBuffer;
    } catch (error) {
        throw new Error(`Failed to export raw key: ${error.message}`);
    }
}
export async function generateKeyPair() {
    const privateKey = crypto.getRandomValues(new Uint8Array(32)); // 32-byte private key
    const publicKey = getPublicKey(privateKey, true); // Compressed public key
    return { privateKey, publicKey };
}
export function deriveWalletAddress(publicKey) {
    const addressHash = keccak_256(publicKey.slice(1)); // Skip 0x04 prefix if uncompressed
    const walletAddress = '0x' + Array.from(addressHash.slice(-20))
        .map(b => b.toString(16).padStart(2, '0'))
        .join('');
    return walletAddress;
}






