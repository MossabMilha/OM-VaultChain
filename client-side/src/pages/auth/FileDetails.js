import { getAllVersionFileApi, getCurrentVersionFileApi } from "../../services/api/FileListingApi.js";
import { getCurrentUser } from "../../utils/userKeyStorage.js";
import { uploadNewVersion } from "../../utils/UploadFiles.js";
import { getUserWithAccessInfoApi } from "../../services/api/Userinformation.js";

// ====== Initialization ======
const params = new URLSearchParams(window.location.search);
const fileId = params.get("id");
const currentUser = await getCurrentUser();

const response = await getCurrentVersionFileApi(currentUser.userId, fileId);
const file = response.file;

// ====== DOM Elements ======
const versionHistoryContainer = document.getElementById("versionHistoryContainer");
const versionHistoryList = document.getElementById("versionHistoryList");
const fileInput = document.getElementById("fileInput");
const accessListContainer = document.getElementById("accessListContainer");
const accessList = document.getElementById("accessList");

// ====== Helper: Render Access List ======
async function renderAccessList(usersWithAccess) {
    if (usersWithAccess.length === 0) {
        accessListContainer.style.display = "block";
        accessList.innerHTML = `
            <div class="no-access-message">
                ❌ No users have access to this file.
            </div>
        `;
        return;
    }

    usersWithAccess.forEach(user => {
        const accessItem = document.createElement("div");
        accessItem.className = "access-item";

        const header = document.createElement("div");
        header.className = "access-header";

        const nameSpan = document.createElement("span");
        nameSpan.className = "access-name";
        nameSpan.textContent = (user.first_name || user.last_name)
            ? `${user.first_name || ""} ${user.last_name || ""}`.trim()
            : (user.username || user.email || "Unknown User");

        const toggleIcon = document.createElement("span");
        toggleIcon.className = "toggle-icon";
        toggleIcon.textContent = "+";

        header.appendChild(nameSpan);
        header.appendChild(toggleIcon);

        const details = document.createElement("div");
        details.className = "access-details";
        details.innerHTML = `
            <strong>ID:</strong> ${user.id}<br>
            <strong>Username:</strong> ${user.username || "N/A"}<br>
            <strong>Email:</strong> ${user.email || "N/A"}<br>
            <strong>Wallet Address:</strong> ${user.wallet_address || "N/A"}<br>
            <strong>Public Key:</strong> ${user.public_key || "N/A"}<br>
        `;

        header.addEventListener("click", () => {
            details.classList.toggle("show");
            toggleIcon.textContent = details.classList.contains("show") ? "−" : "+";
        });

        accessItem.appendChild(header);
        accessItem.appendChild(details);
        accessList.appendChild(accessItem);
    });
}

// ====== Helper: Render Version History ======
function renderVersionHistory(versions) {
    if (!versions || versions.length <= 1) return;

    versionHistoryContainer.style.display = "block";
    versions.sort((a, b) => b.version_number - a.version_number);

    versions.forEach(v => {
        const versionItem = document.createElement("div");
        versionItem.className = "version-item";

        // Header
        const header = document.createElement("div");
        header.className = "version-header";

        const title = document.createElement("span");
        title.textContent = `Version ${v.version_number} - ${new Date(v.created_at).toLocaleString()}`;

        if (v.is_current) {
            const badge = document.createElement("span");
            badge.className = "current-badge";
            badge.textContent = "CURRENT";
            title.appendChild(badge);
        }

        const icon = document.createElement("span");
        icon.className = "toggle-icon";
        icon.textContent = "+";

        header.appendChild(title);
        header.appendChild(icon);

        // Details
        const details = document.createElement("div");
        details.className = "version-details";
        details.innerHTML = `
            <strong>CID:</strong> ${v.cid}<br>
            <strong>File Hash:</strong> ${v.file_hash}<br>
            <strong>Size:</strong> ${(v.size_bytes / 1024).toFixed(2)} KB<br>
            <strong>Encryption:</strong> ${v.encryption_algorithm}<br>
        `;

        // Toggle details on header click
        header.addEventListener("click", () => {
            details.classList.toggle("show");
            icon.textContent = details.classList.contains("show") ? "−" : "+";
        });

        versionItem.appendChild(header);
        versionItem.appendChild(details);
        versionHistoryList.appendChild(versionItem);
    });
}

// ====== Main Logic Based on Access ======
if (response.accessType === "owned") {
    const allVersionResponse = await getAllVersionFileApi(currentUser.userId, fileId);
    const allVersion = allVersionResponse.versions;

    const res = await getUserWithAccessInfoApi(currentUser.userId, fileId);
    const usersWithAccess = Array.isArray(res) ? res : (res?.users || []);

    await renderAccessList(usersWithAccess);
    renderVersionHistory(allVersion);
} else {
    versionHistoryContainer.style.display = "block";
    versionHistoryList.innerHTML = `
        <div class="no-access-message">
            ❌ You do not have access to view the version history of this file.
        </div>
    `;

    accessListContainer.style.display = "block";
    accessList.innerHTML = `
        <div class="no-access-message">
            ❌ You do not have access to view who has access to this file.
        </div>
    `;
}

// ====== Toggle Entire Version History Section ======
const mainHeader = versionHistoryContainer.querySelector(".collapsible-header");
const mainContent = versionHistoryContainer.querySelector(".collapsible-content");
const mainIcon = versionHistoryContainer.querySelector(".toggle-icon");

mainHeader.addEventListener("click", () => {
    const isVisible = mainContent.style.display === "block";
    mainContent.style.display = isVisible ? "none" : "block";
    mainIcon.textContent = isVisible ? "+" : "−";
});

// ====== Populate File Details ======
document.getElementById("fileName").textContent = `📄 ${file.name}`;
document.getElementById("id").textContent = file.id;
document.getElementById("size").textContent = (file.size_bytes / 1024).toFixed(2) + " KB";
document.getElementById("createdAt").textContent = new Date(file.created_at).toLocaleString();
document.getElementById("updatedAt").textContent = new Date(file.updated_at).toLocaleString();
document.getElementById("cid").textContent = file.cid;
document.getElementById("encryptionAlgo").textContent = file.encryption_algorithm;
document.getElementById("fileHash").textContent = file.file_hash;

// ====== Upload Handling ======
document.getElementById("uploadButton").addEventListener("click", () => fileInput.click());

fileInput.addEventListener("change", async (event) => {
    const selectedFile = event.target.files[0];
    if (!selectedFile) return;

    try {
        const result = await uploadNewVersion(selectedFile, fileId);
        console.log(result);
    } catch (err) {
        console.error("Upload failed:", err);
        alert("❌ Failed to upload new version.");
    }
});

// ====== Toggle Who Has Access Section ======
const accessHeader = accessListContainer.querySelector(".collapsible-header");
const accessContent = accessListContainer.querySelector(".collapsible-content");
const accessIcon = accessListContainer.querySelector(".toggle-icon");

accessHeader.addEventListener("click", () => {
    const isVisible = accessContent.style.display === "block";
    accessContent.style.display = isVisible ? "none" : "block";
    accessIcon.textContent = isVisible ? "+" : "−";
});
