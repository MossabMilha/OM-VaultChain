
import {loginUser} from "../../services/api/authService.js";
import {connectWallet} from "../../utils/walletUtils.js";
import {hash512} from "../../crypto/hash.js";
import {deriveEncryptionKey} from "../../crypto/keyDerivation.js";
import {decryptPrivateKey} from "../../crypto/decrypt.js";
import {testEncryptDecrypt} from "../../crypto/encrypt.js";
import {arrayBufferToBase64, base64ToArrayBuffer} from "../../crypto/keyUtils.js";
import {getUserFromLocalStorage, saveUserToLocalStorage} from "../../utils/userKeyStorage.js";

export async function login({ email, password }) {
    try {
        const result = await loginUser({ email, password });
        console.log("Login Success:", result);

        if (result.success) {
            const { userId, firstName, lastName, email, walletAddress, publicKey, encryptedPrivateKey, iv, signupMethod } = result.data;

            if (signupMethod === "wallet") {
                // ✅ Wallet flow
                const wallet = await connectWallet();
                const waAddress = wallet.address;
                if (waAddress.toLowerCase() !== walletAddress.toLowerCase()) {
                    throw new Error("Connected wallet address does not match the registered address");
                }

                const hash = await hash512(walletAddress);
                const backupCode = "OMVC" + hash;
                const encryptionKey = await deriveEncryptionKey(backupCode);
                const decryptedKey = await decryptPrivateKey(base64ToArrayBuffer(encryptedPrivateKey), base64ToArrayBuffer(iv), encryptionKey);

                const success = await testEncryptDecrypt(
                    arrayBufferToBase64(base64ToArrayBuffer(publicKey)),
                    arrayBufferToBase64(decryptedKey)
                );
                if (!success) throw new Error("Encryption/decryption test failed");

                saveUserToLocalStorage({ email, userId, firstName, lastName, walletAddress, publicKey, decryptedKey, signupMethod });

            } else if (signupMethod === "generated") {

                const existingUser = getUserFromLocalStorage(email);
                if (existingUser?.privateKey) { // FIX: Correct property name
                    const decryptedKey = base64ToArrayBuffer(existingUser.privateKey);
                    const success = await testEncryptDecrypt(
                        arrayBufferToBase64(base64ToArrayBuffer(publicKey)),
                        arrayBufferToBase64(decryptedKey)
                    );

                    if (success) {
                        console.log("✅ Local key valid");
                        saveUserToLocalStorage({ email, userId, firstName, lastName, walletAddress, publicKey, decryptedKey, signupMethod });
                        return;
                    } else {
                        console.warn("⚠️ Local key invalid, requesting backup code");
                    }
                }

                // ✅ Prompt for backup code if no valid key
                const backupCode = prompt("Enter your 16-chunk backup code:");
                if (!backupCode) throw new Error("Backup code is required.");

                const encryptionKey = await deriveEncryptionKey(backupCode);


                const decryptedKey = await decryptPrivateKey(base64ToArrayBuffer(encryptedPrivateKey),base64ToArrayBuffer(iv),encryptionKey);

                const success = await testEncryptDecrypt(arrayBufferToBase64(base64ToArrayBuffer(publicKey)), arrayBufferToBase64(decryptedKey));
                console.log("success : ",success);
                if (!success) throw new Error("Backup code invalid: decryption failed.");

                saveUserToLocalStorage({ email, userId, firstName, lastName, walletAddress, publicKey, decryptedKey, signupMethod });
            }
        }
    } catch (error) {
        console.log(error);
    }
}


document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();

    await login({ email, password });
});