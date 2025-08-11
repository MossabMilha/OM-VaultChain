import { arrayBufferToBase64 } from "../crypto/keyUtils.js";

export function saveUserToLocalStorage(userData) {
    const { email, userId, firstName, lastName, walletAddress, publicKey, decryptedKey, signupMethod, rememberMe } = userData;

    const storedUsersJson = localStorage.getItem("VaultChain_Users");
    const storedUsers = storedUsersJson ? JSON.parse(storedUsersJson) : {};

    storedUsers[email] = {
        userId,
        firstName,
        lastName,
        email,
        walletAddress,
        publicKey,
        privateKey: arrayBufferToBase64(decryptedKey),
        signupMethod,
        rememberMe: rememberMe || false,
    };

    localStorage.setItem("VaultChain_Users", JSON.stringify(storedUsers));
}

export function saveCurrentUser(userData) {
    const { email, userId, firstName, lastName, walletAddress, publicKey, decryptedKey, signupMethod, rememberMe } = userData;

    const storedUser = {
        userId,
        firstName,
        lastName,
        email,
        walletAddress,
        publicKey,
        privateKey: arrayBufferToBase64(decryptedKey),
        signupMethod,
        rememberMe: rememberMe || false,
    };
    localStorage.setItem("VaultChain_Current_User", JSON.stringify(storedUser));
}

export function getUserFromLocalStorage(email) {
    const storeUsersJson = localStorage.getItem("VaultChain_Users");
    const storeUsers = storeUsersJson ? JSON.parse(storeUsersJson) : {};
    return storeUsers[email] || null;
}

export function getCurrentUser() {
    const storedUserJSON = localStorage.getItem("VaultChain_Current_User");
    return storedUserJSON ? JSON.parse(storedUserJSON) : null;
}

export function clearUserByEmail(email) {
    const storedUserJSON = localStorage.getItem("VaultChain_Users");
    if (!storedUserJSON) return;
    const storeUsers = JSON.parse(storedUserJSON);
    if (storeUsers[email]) {
        delete storeUsers[email];
        localStorage.setItem("VaultChain_Users", JSON.stringify(storeUsers));

        const currentUser = getCurrentUser();
        if (currentUser?.email === email) {
            localStorage.removeItem("VaultChain_Current_User");
        }
    }
}

export function clearAllUsers() {
    localStorage.removeItem("VaultChain_Users");
}
