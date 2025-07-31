export async function deriveEncryptionKey(source){
    const encoder = new TextEncoder();
    const keyMaterial =encoder.encode(source);
    const baseKey = await crypto.subtle.importKey(
        "raw",
        keyMaterial,
        {name:"PBKDF2"},
        false,
        ['deriveBits', 'deriveKey']
    );
    return await crypto.subtle.deriveKey(
        {
            name:"PBKDF2",
            salt: encoder.encode('om-vault-chain-salt-v1-dexter-was-here'),
            iterations: 100000,
            hash: 'SHA-512'
        },
        baseKey,
        {name:"AES-GCM",length:256},
        false,
        ["encrypt","decrypt"]
    );
}