import { loginUser } from "../../services/api/authService.js";
import { connectWallet } from "../../utils/walletUtils.js";
import { hash512 } from "../../crypto/hash.js";
import { deriveEncryptionKey } from "../../crypto/keyDerivation.js";
import { decryptPrivateKey } from "../../crypto/decrypt.js";
import { testEncryptDecrypt } from "../../crypto/encrypt.js";
import { arrayBufferToBase64, base64ToArrayBuffer } from "../../crypto/keyUtils.js";
import { getUserFromLocalStorage, saveCurrentUser, saveUserToLocalStorage } from "../../utils/userKeyStorage.js";

// Modal elements
const backupCodeModal = document.getElementById("backupCodeModal");
const modalOverlay = document.getElementById("modalOverlay");
const modalCloseBtn = document.getElementById("modalCloseBtn");
const backupCodeInput = document.getElementById("backupCodeInput");
const backupCodeSubmit = document.getElementById("backupCodeSubmit");

// Open modal and set submit handler
function openBackupCodeModal(onSubmit) {
    backupCodeModal.classList.remove("hidden");
    modalOverlay.classList.remove("hidden");
    backupCodeInput.focus();

    // Remove any previous submit handler
    document.getElementById("backupCodeForm").onsubmit = async (e) => {
        e.preventDefault();
        const backupCode = backupCodeInput.value;  // Keep dashes as is!
        if (backupCode.length !== 79) {  // 16 chunks * 4 chars + 15 dashes = 79 chars total
            alert("Backup code must be exactly 16 chunks with dashes (e.g. XXXX-XXXX-...)");
            return;
        }
        try {
            await onSubmit(backupCode);
            closeBackupCodeModal();
        } catch (err) {
            alert(err.message || "Invalid backup code.");
        }
    };
}

function closeBackupCodeModal() {
    backupCodeModal.classList.add("hidden");
    modalOverlay.classList.add("hidden");
    backupCodeInput.value = "";
    backupCodeSubmit.disabled = true;
}

modalCloseBtn.addEventListener("click", closeBackupCodeModal);
modalOverlay.addEventListener("click", closeBackupCodeModal);
document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") closeBackupCodeModal();
});

backupCodeInput.addEventListener("input", (e) => {
    // Auto-format input as XXXX-XXXX-...
    let raw = e.target.value.toUpperCase().replace(/[^A-Z0-9]/g, "");
    let formatted = raw.match(/.{1,4}/g)?.join("-") || "";
    e.target.value = formatted;
    backupCodeSubmit.disabled = (formatted.length !== 79);
});

// Main login function
export async function login({ email, password, rememberMe }) {
    try {
        const result = await loginUser({ email, password });
        console.log("Login Success:", result);

        if (result.success) {
            const { userId, firstName, lastName, email, walletAddress, publicKey, encryptedPrivateKey, iv, signupMethod } = result.data;

            if (signupMethod === "wallet") {
                const wallet = await connectWallet();
                const waAddress = wallet.address;
                if (waAddress.toLowerCase() !== walletAddress.toLowerCase()) {
                    throw new Error("Connected wallet address does not match the registered address");
                }

                const hash = await hash512(walletAddress);
                const backupCode = "OMVC" + hash;
                const encryptionKey = await deriveEncryptionKey(backupCode);
                const decryptedKey = await decryptPrivateKey(
                    base64ToArrayBuffer(encryptedPrivateKey),
                    base64ToArrayBuffer(iv),
                    encryptionKey
                );

                const success = await testEncryptDecrypt(
                    arrayBufferToBase64(base64ToArrayBuffer(publicKey)),
                    arrayBufferToBase64(decryptedKey)
                );
                if (!success) throw new Error("Encryption/decryption test failed");

                const userData = { email, userId, firstName, lastName, walletAddress, publicKey, decryptedKey, signupMethod, rememberMe };
                saveUserToLocalStorage(userData);
                saveCurrentUser(userData);
                window.location.href = "/Dashboard.html";

            } else if (signupMethod === "generated") {
                const existingUser = getUserFromLocalStorage(email);
                if (existingUser?.privateKey) {
                    const decryptedKey = base64ToArrayBuffer(existingUser.privateKey);
                    const success = await testEncryptDecrypt(
                        arrayBufferToBase64(base64ToArrayBuffer(publicKey)),
                        arrayBufferToBase64(decryptedKey)
                    );

                    if (success) {
                        const userData = { email, userId, firstName, lastName, walletAddress, publicKey, decryptedKey, signupMethod, rememberMe };
                        saveUserToLocalStorage(userData);
                        saveCurrentUser(userData);
                        window.location.href = "Dashboard.html";
                        return;
                    } else {
                        console.warn("⚠️ Local key invalid, requesting backup code");
                    }
                }

                openBackupCodeModal(async (backupCode) => {
                    const encryptionKey = await deriveEncryptionKey(backupCode);
                    const decryptedKey = await decryptPrivateKey(
                        base64ToArrayBuffer(encryptedPrivateKey),
                        base64ToArrayBuffer(iv),
                        encryptionKey
                    );

                    const success = await testEncryptDecrypt(
                        arrayBufferToBase64(base64ToArrayBuffer(publicKey)),
                        arrayBufferToBase64(decryptedKey)
                    );
                    if (!success) throw new Error("Backup code invalid: decryption failed.");

                    const userData = { email, userId, firstName, lastName, walletAddress, publicKey, decryptedKey, signupMethod, rememberMe };
                    saveUserToLocalStorage(userData);
                    saveCurrentUser(userData);
                    window.location.href = "Dashboard.html";
                });
            }
        }
    } catch (error) {
        console.error(error);
        alert(error.message || "Login failed. Please try again.");
    }
}

// Attach login handler to form submit
document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const rememberMe = document.getElementById("remember").checked;
    await login({ email, password ,rememberMe});
});
