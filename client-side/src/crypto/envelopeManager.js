import { base64ToArrayBuffer, arrayBufferToBase64} from "./keyUtils.js";
export async function importRSAPublicKey(pem) {
    const pemHeader = "-----BEGIN PUBLIC KEY-----";
    const pemFooter = "-----END PUBLIC KEY-----";
    const pemContent = pem.replace(pemHeader, "").replace(pemFooter, "").replace(/\s+/g, "");
    const binaryDer = base64ToArrayBuffer(pemContent);

    return crypto.subtle.importKey("spki",binaryDer,{name: "RSA-OAEP",hash: "SHA-512"},true,["encrypt"]);
}

export async function wrapAESKeyWithPublicKey(AESKey, RSAPublicKeyPem) {
    const rawAESKey = base64ToArrayBuffer(AESKey);
    const publicKey = await importRSAPublicKey(RSAPublicKeyPem);
    const encryptedKey = await crypto.subtle.encrypt({name: "RSA-OAEP"}, publicKey, rawAESKey);
    return arrayBufferToBase64(encryptedKey);
}