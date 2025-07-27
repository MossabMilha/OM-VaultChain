import { base64ToArrayBuffer, arrayBufferToBase64} from "./keyUtils.js";
export async function importRSAPublicKey(pem) {
    const pemHeader = "-----BEGIN PUBLIC KEY-----";
    const pemFooter = "-----END PUBLIC KEY-----";

    // More robust cleaning: remove headers, footers, and all whitespace/newlines
    let pemContent = pem.trim();
    pemContent = pemContent.replace(pemHeader, "");
    pemContent = pemContent.replace(pemFooter, "");
    pemContent = pemContent.replace(/[\r\n\s]/g, ""); // Remove all whitespace, newlines, carriage returns

    try {
        const binaryDer = base64ToArrayBuffer(pemContent);
        return crypto.subtle.importKey("spki", binaryDer, {name: "RSA-OAEP", hash: "SHA-512"}, true, ["encrypt"]);
    } catch (error) {
        throw new Error("Invalid PEM format: " + error.message);
    }
}

export async function wrapAESKeyWithPublicKey(AESKeyBase64, RSAPublicKeyPem) {
    try {
        if (!AESKeyBase64) {
            throw new Error('AES key base64 string is required');
        }
        const rawAESKey = base64ToArrayBuffer(AESKeyBase64);
        const publicKey = await importRSAPublicKey(RSAPublicKeyPem);
        const encryptedKey = await crypto.subtle.encrypt({name: "RSA-OAEP"}, publicKey, rawAESKey);
        return arrayBufferToBase64(encryptedKey);
    } catch (error) {
        throw error;
    }
}
