import {generateKeyPair,deriveWalletAddress} from "../../crypto/keyUtils.js";
import {deriveEncryptionKey} from "../../crypto/keyDerivation.js";
import {encryptPrivateKey,testEncryptDecrypt} from "../../crypto/encrypt.js";
import {generateBackupCode} from "../../crypto/backupCodeUtils.js";
import {connectWallet,signMessage} from "../../utils/walletUtils.js";
import {signupUser} from "../../services/api/authService.js";
import {hash512} from "../../crypto/hash.js";
import {arrayBufferToBase64} from "../../crypto/keyUtils.js";
import {decryptPrivateKey} from "../../crypto/decrypt.js";


export async function signUp({ firstName, lastName, email, password, method }) {
    try {
        // Step 1: Get real wallet address
        let walletAddress = null;
        let backupCode = null;
        let source = null;
        let privateKey, publicKey;

        if (method === "wallet") {
            const wallet = await connectWallet();
            walletAddress = wallet.address;
            ({ privateKey, publicKey } = await generateKeyPair());
            const signatureMessage = `OM VaultChain Key Derivation\nWallet: ${walletAddress}\nTimestamp: ${Date.now()}`;
            await signMessage(signatureMessage, walletAddress);

            // Deterministic backup code from walletAddress + password
            const hash = await hash512(walletAddress);
            backupCode = "OMVC" + hash;
            source = backupCode;

        } else if (method === "generated") {
            ({ privateKey, publicKey } = await generateKeyPair());

            walletAddress = deriveWalletAddress(publicKey, privateKey);

            backupCode = generateBackupCode();
            alert("📌 IMPORTANT: Save this backup code securely!\n\n" + backupCode);

            source = backupCode;
        }

        // Step 2: Derive AES encryption key from backupCode
        const encryptionKey = await deriveEncryptionKey(source);


        // Step 3: Encrypt private key with AES key
        const { encryptedPrivateKey, iv } = await encryptPrivateKey(privateKey, encryptionKey);

        const decryptedKey = await decryptPrivateKey(encryptedPrivateKey,iv,encryptionKey)

        const success = await testEncryptDecrypt(arrayBufferToBase64(publicKey), arrayBufferToBase64(decryptedKey));
        if (!success) {
            throw new Error("Encryption/decryption test failed");
        }

            // Step 4: Prepare payload
        const payload = {
            firstName,
            lastName,
            email,
            password,
            walletAddress,
            publicKey:arrayBufferToBase64(publicKey),
            encryptedPrivateKey: arrayBufferToBase64(encryptedPrivateKey),
            iv: arrayBufferToBase64(iv),
            signup_method: method,
        };
        const response = await signupUser(payload);
        if (!response || response.success === false) {
            throw new Error(response?.message || "Signup failed");
        } else {
            alert("🎉 Signup successful! Please log in.");
        }

    } catch (error) {
        console.error("❌ Signup error:", error);
        alert("Signup failed: " + error.message);
    }
}

document.getElementById("signupForm").addEventListener("submit", async (e)=>{
    e.preventDefault();
    const firstName = document.getElementById('firstName').value.trim();
    const lastName = document.getElementById('lastName').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const method = document.querySelector('input[name="signupMethod"]:checked').value.trim();

    await signUp({firstName,lastName,email,password,method});

});