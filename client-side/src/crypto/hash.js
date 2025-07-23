import { arrayBufferToBase64 } from "./keyUtils.js";

export async function hashFile(file){
    const buffer = await file.arrayBuffer();
    const hashBuffer = await crypto.subtle.digest("SHA-512", buffer);
    return arrayBufferToBase64(hashBuffer);
}