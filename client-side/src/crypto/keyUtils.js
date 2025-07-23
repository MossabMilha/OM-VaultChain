export async function generateAESKey() {
    return crypto.subtle.generateKey({name: "AES-GCM",length: 256},true, ["encrypt", "decrypt"]);
}

export function arrayBufferToBase64(buffer) {
    const bytes = new Uint8Array(buffer);
    let binary = '';
    for(let b of bytes) binary += String.fromCharCode(b);
    return btoa(binary);
}
export async function exportRawKey(key) {
    const raw = await crypto.subtle.exportKey("raw", key);
    return new Uint8Array(raw);
}
export function base64ToArrayBuffer(base64) {
    const binary = atob(base64);
    const bytes = new Uint8Array(binary.length);
    for (let i = 0; i < binary.length; i++) bytes[i] = binary.charCodeAt(i);
    return bytes.buffer;
}
