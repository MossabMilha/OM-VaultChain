import { arrayBufferToBase64 } from "./keyUtils.js";

export async function hashFile(file){
    const buffer = await file.arrayBuffer();
    const hashBuffer = await crypto.subtle.digest("SHA-512", buffer);
    return arrayBufferToBase64(hashBuffer);
}
export async function hash512(data){
    const encoder = new TextEncoder();
    const encodedData = encoder.encode(data);
    const hashBuffer = await crypto.subtle.digest("SHA-512", encodedData);
    return arrayBufferToBase64(hashBuffer);
}