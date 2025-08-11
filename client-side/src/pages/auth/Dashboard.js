import { getCurrentUser } from "../../utils/userKeyStorage.js";
import { logoutUser } from "../../utils/Logout.js";

document.addEventListener("DOMContentLoaded", () => {
    const user = getCurrentUser();

    // Profile info
    const avatar = document.getElementById("userAvatar");
    const nameEl = document.getElementById("userName");
    const emailEl = document.getElementById("userEmail");

    if (user) {
        const initials = `${user.firstName?.[0] || ""}${user.lastName?.[0] || ""}`.toUpperCase();
        avatar.textContent = initials || "?";
        nameEl.textContent = `${user.firstName || ""} ${user.lastName || ""}`.trim() || "Guest";
        emailEl.textContent = user.email || "";
    } else {
        avatar.textContent = "?";
        nameEl.textContent = "Guest";
        emailEl.textContent = "";
    }

    // File Listing redirect
    const fileListingBtn = document.getElementById("fileListingBtn");
    if (fileListingBtn) {
        fileListingBtn.addEventListener("click", (e) => {
            e.preventDefault();
            window.location.href = "FileListing.html";
        });
    }

    // Overlay logic
    const logoutBtn = document.getElementById("logoutBtn");
    const overlay = document.getElementById("logoutOverlay");
    const keepBtn = document.getElementById("keepRememberedBtn");
    const removeBtn = document.getElementById("removeRememberedBtn");
    const cancelBtn = document.getElementById("cancelLogoutBtn");

    logoutBtn.addEventListener("click", () => {
        if (user?.signupMethod === "generated" && user.rememberMe) {
            overlay.classList.remove("hidden");
        } else {
            logoutUser(false);
            window.location.href = "login.html";
        }
    });

    keepBtn.addEventListener("click", () => {
        logoutUser(true);
        window.location.href = "login.html";
    });

    removeBtn.addEventListener("click", () => {
        logoutUser(false);
        window.location.href = "login.html";
    });

    cancelBtn.addEventListener("click", () => {
        overlay.classList.add("hidden");
    });
});