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
export async function importRawKey(RawKeyBuffer) {
    return await crypto.subtle.importKey("raw",RawKeyBuffer,"AES-GCM",true,["decrypt"]);

}
export async function exportRawKey(cryptoKey) {
    try {
        const rawKeyBuffer = await crypto.subtle.exportKey("raw", cryptoKey);
        return rawKeyBuffer;
    } catch (error) {
        throw new Error(`Failed to export raw key: ${error.message}`);
    }
}
export async function importRSAPrivateKey(privateKeyPem) {
    try {
        const pemContents = privateKeyPem
            .replace(/-----BEGIN PRIVATE KEY-----/, '')
            .replace(/-----END PRIVATE KEY-----/, '')
            .replace(/\s/g, '');
        
        const keyBuffer = base64ToArrayBuffer(pemContents);
        
        return await crypto.subtle.importKey(
            'pkcs8',
            keyBuffer,
            {
                name: 'RSA-OAEP',
                hash: 'SHA-512'  // Changed from SHA-256 to SHA-512
            },
            false,
            ['decrypt']
        );
    } catch (error) {
        throw new Error(`Failed to import RSA private key: ${error.message}`);
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
