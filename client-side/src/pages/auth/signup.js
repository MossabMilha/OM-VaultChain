import {generateEncryptionKeyPair, exportPublicKeyToBase64,exportPrivateKeyToBase64,} from "../../crypto/keyUtils.js";
import { generateBackupCode } from "../../crypto/backupCodeUtils.js";
import { deriveKeyFromBackupCode, encryptPrivateKeyAES } from "../../crypto/encrypt.js";
import { signupUser } from "../../services/api/authService.js";

document.getElementById("signup-form").addEventListener("submit", async(e) => {
    e.preventDefault();

    const firstName = document.getElementById("first_name").value;
    const lastName = document.getElementById("last_name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    try{
        //1. Generate Encryption key pair
        const { publicKey, privateKey } = await generateEncryptionKeyPair();
        //2. Export public key to base64
        const publicKeyBase64 = await exportPublicKeyToBase64(publicKey);
        const privateKeyBase64 = await exportPrivateKeyToBase64(privateKey);
        //3. Generate backup code
        const backupCode = generateBackupCode();
        //4. Derive AES key from backup code
        const AESKey = await deriveKeyFromBackupCode(backupCode);
        //5. Encrypt private key with AES key
        const {encryptedPrivateKey,iv} = await encryptPrivateKeyAES(privateKeyBase64, AESKey);
        console.log(publicKeyBase64)
        console.log(encryptedPrivateKey);
        alert("IMPORTANT: Save this backup code!\n\n" + backupCode);

        const response = await signupUser({
            firstName,
            lastName,
            email,
            password,
            publicKey: publicKeyBase64,
            encryptedPrivateKey,
            iv
        });

        console.log("Full API Response:", response);

        if (response.success) {
            alert("✅ Signup successful!");
            // Optional: redirect to login page
        } else {
            console.error("Signup failed. Full response:", response);
            alert("❌ Signup failed: " + (response.message || response.error || JSON.stringify(response)));
        }
    }catch (error) {
        console.error("Signup error:", error);
        alert("⚠️ Something went wrong during signup.");

    }

})