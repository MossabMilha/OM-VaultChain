// import { loginUser } from "../../services/api/authService.js";
// import { handleWalletLogin } from "../../services/auth/walletLoginHandler.js";
// import { encryptWithPublicKey,decryptWithPrivateKey} from "../../crypto/encrypt.js";
// import {importRSAPublicKey,importRSAPrivateKey} from "../../crypto/keyUtils.js";
// import {connectWallet} from "../../utils/walletUtils.js";
//
//
// async function login(email, password) {
//     const response = await loginUser({ email, password });
//
//     if (!response || response.success === false) {
//         throw new Error(response.message || "Login failed");
//     }
//
//     const { token, data } = response;
//     const { walletAddress, encryptedPrivateKey, iv, signupMethod, publicKey, userId, firstName, lastName } = data;
//
//     if (signupMethod === "wallet") {
//         try {
//             const address = await connectWallet();
//             console.log(address);
//             if(!address || address.toLowerCase() !== walletAddress.toLowerCase()){
//                 throw new Error("Connected wallet does not match the registered address.");
//             }
//
//
//
//
//             const privateKey = await handleWalletLogin({
//                 walletAddress,
//                 email,
//                 encryptedPrivateKey,
//                 iv
//             });
//             console.log("PrivateKey :",privateKey);
//             console.log("PrivateKey length:", privateKey.length);
//             console.log("PrivateKey start:", privateKey.slice(0, 10));
//             console.log("PrivateKey end:", privateKey.slice(-10));
//             console.log(publicKey);
//
//             // Ensure privateKey is PKCS#8 format
//             if (!privateKey.startsWith("-----BEGIN PRIVATE KEY-----") && !privateKey.startsWith("MII")) {
//                 throw new Error("Private key must be in PKCS#8 format (-----BEGIN PRIVATE KEY-----)");
//             }
//             // Normalize privateKey: remove whitespace, ensure PEM header/footer
//             let normalizedKey = privateKey.trim();
//             if (!normalizedKey.startsWith("-----BEGIN PRIVATE KEY-----")) {
//                 // If it's base64, wrap with PEM header/footer
//                 normalizedKey = `-----BEGIN PRIVATE KEY-----\n${normalizedKey.replace(/\s+/g, '').match(/.{1,64}/g).join('\n')}\n-----END PRIVATE KEY-----`;
//             }
//             const importedPrivateKey = await importRSAPrivateKey(normalizedKey);
//             const importedPublicKey = await importRSAPublicKey(publicKey);
//             const encryptedData = await encryptWithPublicKey(importedPublicKey,"hello")
//             const decryptedData = await decryptWithPrivateKey(importedPrivateKey, encryptedData);
//
//
//
//             console.log(decryptedData);
//             if (decryptedData === "hello") {
//                 console.log("✅ Encryption/decryption verified");
//             } else {
//                 console.warn("⚠️ Encryption/decryption failed");
//             }
//
//             return {
//                 userId,
//                 firstName,
//                 lastName,
//                 email,
//                 walletAddress,
//                 publicKey,
//                 privateKey,
//                 signupMethod,
//                 token
//             };
//         } catch (error) {
//             console.error("Wallet login failed:", error.message);
//             throw error;
//         }
//
//     } else if (signupMethod === "generated") {
//         console.warn("Generated account login not yet implemented");
//         throw new Error("Generated account login not supported yet.");
//     } else {
//         throw new Error("Unknown signup method.");
//     }
// }
//
// document.getElementById("loginForm").addEventListener("submit", async (e) => {
//     e.preventDefault();
//
//     const email = document.getElementById("email").value.trim();
//     const password = document.getElementById("password").value.trim();
//     try {
//         await login(email, password);
//     } catch (err) {
//         alert("❌ " + err.message);
//         console.error(err);
//     }
// });
