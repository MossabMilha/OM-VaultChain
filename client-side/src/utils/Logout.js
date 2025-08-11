import {getCurrentUser,clearUserByEmail} from "./userKeyStorage.js";


export function logoutUser(keepRemembered = false) {
    const currentUser = getCurrentUser();
    if (!currentUser) return;

    if (keepRemembered) {
        // Just remove current session (logout)
        localStorage.removeItem("VaultChain_Current_User");
    } else {
        // Remove user completely (from VaultChain_Users and current session)
        clearUserByEmail(currentUser.email);
    }
}