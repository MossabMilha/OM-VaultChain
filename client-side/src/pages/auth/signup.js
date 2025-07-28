import {generateEncryptionKeyPair, exportPublicKeyToBase64,exportPrivateKeyToBase64,} from "../../crypto/keyUtils.js";
import { generateBackupCode } from "../../crypto/backupCodeUtils.js";
import { deriveKeyFromBackupCode, encryptPrivateKeyAES } from "../../crypto/encrypt.js";
import { signupUser } from "../../services/api/authService.js";
import {storePrivateKey} from "../../utils/userKeyStorage.js";



async function signupWithBackupCode(firstName, lastName, email, password) {
    try {
        // 1. Generate Encryption key pair
        const { publicKey, privateKey } = await generateEncryptionKeyPair();

        // 2. Export keys to base64
        const publicKeyBase64 = await exportPublicKeyToBase64(publicKey);
        const privateKeyBase64 = await exportPrivateKeyToBase64(privateKey);

        // 3. Generate backup code and derive AES key
        const backupCode = generateBackupCode();
        const AESKey = await deriveKeyFromBackupCode(backupCode);

        // 4. Encrypt the private key
        const { encryptedPrivateKey, iv } = await encryptPrivateKeyAES(privateKeyBase64, AESKey);

        // 5. Notify user to save the backup code
        alert("📌 IMPORTANT: Save this backup code securely!\n\n" + backupCode);

        // 6. Send signup request
        const response = await signupUser({
            firstName,
            lastName,
            email,
            password,
            publicKey: publicKeyBase64,
            encryptedPrivateKey,
            iv
        });

        if (!response.success) {
            console.error("Signup failed:", response);
            alert("❌ Signup failed: " + (response.message || response.error || JSON.stringify(response)));
            return;
        }

        // 7. Store the private key locally
        storePrivateKey(email, privateKeyBase64, publicKeyBase64);

    } catch (error) {
        console.error("Signup error:", error);
        alert("⚠️ An error occurred during signup.");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("signup-form");

    if (!form) {
        console.error("Signup form not found.");
        return;
    }

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const firstName = document.getElementById("first_name").value;
        const lastName = document.getElementById("last_name").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        await signupWithBackupCode(firstName, lastName, email, password);
    });
});