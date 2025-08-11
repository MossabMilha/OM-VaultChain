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
const versionHistoryContent = document.getElementById("versionHistoryContent");
const versionHistoryList = document.getElementById("versionHistoryList");
const versionCount = document.getElementById("versionCount");

const accessListContainer = document.getElementById("accessListContainer");
const accessListContent = document.getElementById("accessListContent");
const accessList = document.getElementById("accessList");
const accessCount = document.getElementById("accessCount");

const fileInput = document.getElementById("fileInput");
const uploadButton = document.getElementById("uploadButton");

// ====== Helper Functions ======

/**
 * Toggle section visibility with smooth animation
 */
function toggleSection(header, content) {
    const isActive = content.classList.contains('active');

    if (isActive) {
        content.classList.remove('active');
        header.classList.remove('active');
    } else {
        content.classList.add('active');
        header.classList.add('active');
    }
}

/**
 * Create a no-access message element
 */
function createNoAccessMessage(message, iconClass = 'fa-exclamation-triangle') {
    return `
        <div class="no-access-message">
            <i class="fas ${iconClass}"></i>
            ${message}
        </div>
    `;
}

/**
 * Format file size in a human-readable format
 */
function formatFileSize(bytes) {
    if (bytes === 0) return '0 B';

    const units = ['B', 'KB', 'MB', 'GB', 'TB'];
    let size = bytes;
    let unitIndex = 0;

    while (size >= 1024 && unitIndex < units.length - 1) {
        size /= 1024;
        unitIndex++;
    }

    return `${size.toFixed(size < 10 && unitIndex > 0 ? 1 : 0)} ${units[unitIndex]}`;
}

/**
 * Format date in a user-friendly format
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now - date);
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays === 0) {
        return 'Today, ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    } else if (diffDays === 1) {
        return 'Yesterday, ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    } else if (diffDays < 7) {
        return `${diffDays} days ago, ` + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    } else {
        return date.toLocaleDateString() + ', ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    }
}

/**
 * Truncate long strings with ellipsis
 */
function truncateString(str, maxLength = 50) {
    if (!str || str.length <= maxLength) return str || 'N/A';
    return str.substring(0, maxLength) + '...';
}

// ====== Render Access List ======
async function renderAccessList(usersWithAccess) {
    if (!usersWithAccess || usersWithAccess.length === 0) {
        accessList.innerHTML = createNoAccessMessage(
            'No users have access to this file.',
            'fa-users-slash'
        );
        accessCount.textContent = '0 users';
        return;
    }

    accessCount.textContent = `${usersWithAccess.length} user${usersWithAccess.length !== 1 ? 's' : ''}`;

    const accessItems = usersWithAccess.map(user => {
        const displayName = (user.first_name || user.last_name)
            ? `${user.first_name || ""} ${user.last_name || ""}`.trim()
            : (user.username || user.email || "Unknown User");

        return `
            <div class="access-item">
                <div class="access-header" onclick="toggleAccessItem(this)">
                    <div class="access-name">
                        <i class="fas fa-user"></i>
                        ${displayName}
                    </div>
                    <i class="fas fa-chevron-down toggle-icon"></i>
                </div>
                <div class="access-details">
                    <div><strong>ID:</strong> ${user.id}</div>
                    <div><strong>Username:</strong> ${user.username || "N/A"}</div>
                    <div><strong>Email:</strong> ${user.email || "N/A"}</div>
                    <div><strong>Wallet Address:</strong> ${truncateString(user.wallet_address, 30)}</div>
                    <div><strong>Public Key:</strong> ${truncateString(user.public_key, 30)}</div>
                </div>
            </div>
        `;
    }).join('');

    accessList.innerHTML = accessItems;
}

// ====== Render Version History ======
function renderVersionHistory(versions) {
    if (!versions || versions.length <= 1) {
        versionHistoryList.innerHTML = createNoAccessMessage(
            'No version history available.',
            'fa-history'
        );
        versionCount.textContent = '0 versions';
        return;
    }

    const sortedVersions = [...versions].sort((a, b) => b.version_number - a.version_number);
    versionCount.textContent = `${sortedVersions.length} version${sortedVersions.length !== 1 ? 's' : ''}`;

    const versionItems = sortedVersions.map(version => {
        const currentBadge = version.is_current
            ? '<span class="current-badge">Current</span>'
            : '';

        return `
            <div class="version-item">
                <div class="version-header" onclick="toggleVersionItem(this)">
                    <div class="version-title">
                        <i class="fas fa-code-branch"></i>
                        Version ${version.version_number} - ${formatDate(version.created_at)}
                        ${currentBadge}
                    </div>
                    <i class="fas fa-chevron-down toggle-icon"></i>
                </div>
                <div class="version-details">
                    <div><strong>CID:</strong> <code>${version.cid}</code></div>
                    <div><strong>File Hash:</strong> <code>${truncateString(version.file_hash, 40)}</code></div>
                    <div><strong>Size:</strong> ${formatFileSize(version.size_bytes)}</div>
                    <div><strong>Encryption:</strong> ${version.encryption_algorithm}</div>
                    <div><strong>Created:</strong> ${new Date(version.created_at).toLocaleString()}</div>
                </div>
            </div>
        `;
    }).join('');

    versionHistoryList.innerHTML = versionItems;
}

// ====== Toggle Functions for Global Access ======
window.toggleVersionItem = function(header) {
    const details = header.nextElementSibling;
    const icon = header.querySelector('.toggle-icon');

    if (details.classList.contains('active')) {
        details.classList.remove('active');
        icon.style.transform = 'rotate(0deg)';
    } else {
        details.classList.add('active');
        icon.style.transform = 'rotate(180deg)';
    }
};

window.toggleAccessItem = function(header) {
    const details = header.nextElementSibling;
    const icon = header.querySelector('.toggle-icon');

    if (details.classList.contains('active')) {
        details.classList.remove('active');
        icon.style.transform = 'rotate(0deg)';
    } else {
        details.classList.add('active');
        icon.style.transform = 'rotate(180deg)';
    }
};

// ====== Section Header Click Handlers ======
function initializeSectionTogles() {
    // Version History Section Toggle
    const versionHeader = versionHistoryContainer.querySelector('.section-header');
    if (versionHeader) {
        versionHeader.addEventListener('click', () => {
            toggleSection(versionHeader, versionHistoryContent);
        });
    }

    // Access List Section Toggle
    const accessHeader = accessListContainer.querySelector('.section-header');
    if (accessHeader) {
        accessHeader.addEventListener('click', () => {
            toggleSection(accessHeader, accessListContent);
        });
    }
}

// ====== File Upload Handling ======
function initializeUpload() {
    if (uploadButton) {
        uploadButton.addEventListener("click", () => {
            fileInput.click();
        });
    }

    if (fileInput) {
        fileInput.addEventListener("change", async (event) => {
            const selectedFile = event.target.files[0];
            if (!selectedFile) return;

            // Show loading state
            const originalText = uploadButton.innerHTML;
            uploadButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Uploading...';
            uploadButton.disabled = true;

            try {
                const result = await uploadNewVersion(selectedFile, fileId);
                console.log("Upload successful:", result);

                // Show success message
                uploadButton.innerHTML = '<i class="fas fa-check"></i> Uploaded!';
                uploadButton.style.background = 'var(--success-color)';

                // Refresh the page after a short delay to show updated data
                setTimeout(() => {
                    window.location.reload();
                }, 1500);

            } catch (err) {
                console.error("Upload failed:", err);

                // Show error state
                uploadButton.innerHTML = '<i class="fas fa-exclamation-triangle"></i> Upload Failed';
                uploadButton.style.background = 'var(--error-color)';

                alert("❌ Failed to upload new version. Please try again.");

                // Reset button after delay
                setTimeout(() => {
                    uploadButton.innerHTML = originalText;
                    uploadButton.style.background = '';
                    uploadButton.disabled = false;
                }, 3000);
            }

            // Clear file input
            fileInput.value = '';
        });
    }
}

// ====== Populate File Details ======
function populateFileDetails() {
    const elements = {
        fileName: document.getElementById("fileName"),
        id: document.getElementById("id"),
        size: document.getElementById("size"),
        createdAt: document.getElementById("createdAt"),
        updatedAt: document.getElementById("updatedAt"),
        cid: document.getElementById("cid"),
        encryptionAlgo: document.getElementById("encryptionAlgo"),
        fileHash: document.getElementById("fileHash")
    };

    // Update file details
    if (elements.fileName) elements.fileName.textContent = file.name;
    if (elements.id) elements.id.textContent = file.id;
    if (elements.size) elements.size.textContent = formatFileSize(file.size_bytes);
    if (elements.createdAt) elements.createdAt.textContent = new Date(file.created_at).toLocaleString();
    if (elements.updatedAt) elements.updatedAt.textContent = new Date(file.updated_at).toLocaleString();
    if (elements.cid) elements.cid.textContent = file.cid;
    if (elements.encryptionAlgo) elements.encryptionAlgo.textContent = file.encryption_algorithm;
    if (elements.fileHash) elements.fileHash.textContent = file.file_hash;
}

// ====== Main Execution ======
async function initializeFileDetails() {
    try {
        // Populate basic file information
        populateFileDetails();

        // Initialize UI components
        initializeSectionTogles();

        // Handle access-based functionality
        if (response.accessType === "owned") {
            // User owns the file - load full data
            initializeUpload();

            try {
                // Load version history
                const allVersionResponse = await getAllVersionFileApi(currentUser.userId, fileId);
                const allVersions = allVersionResponse.versions;
                renderVersionHistory(allVersions);

                // Load access list
                const res = await getUserWithAccessInfoApi(currentUser.userId, fileId);
                const usersWithAccess = Array.isArray(res) ? res : (res?.users || []);
                await renderAccessList(usersWithAccess);

            } catch (error) {
                console.error("Error loading file data:", error);

                versionHistoryList.innerHTML = createNoAccessMessage(
                    'Error loading version history.',
                    'fa-exclamation-circle'
                );

                accessList.innerHTML = createNoAccessMessage(
                    'Error loading access information.',
                    'fa-exclamation-circle'
                );
            }
        } else {
            // User doesn't own the file - show restricted access
            versionHistoryList.innerHTML = createNoAccessMessage(
                'You do not have access to view the version history of this file.',
                'fa-lock'
            );

            accessList.innerHTML = createNoAccessMessage(
                'You do not have access to view who has access to this file.',
                'fa-lock'
            );

            // Disable upload functionality
            if (uploadButton) {
                uploadButton.disabled = true;
                uploadButton.innerHTML = '<i class="fas fa-lock"></i> Access Denied';
                uploadButton.style.opacity = '0.5';
                uploadButton.style.cursor = 'not-allowed';
            }
        }

        console.log("File details initialized successfully");

    } catch (error) {
        console.error("Error initializing file details:", error);

        // Show error state in UI
        const errorMessage = createNoAccessMessage(
            'Error loading file details. Please refresh the page.',
            'fa-exclamation-circle'
        );

        if (versionHistoryList) versionHistoryList.innerHTML = errorMessage;
        if (accessList) accessList.innerHTML = errorMessage;
    }
}

// ====== Initialize Everything ======
document.addEventListener('DOMContentLoaded', () => {
    initializeFileDetails();
});

// Also run immediately if DOM is already loaded
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeFileDetails);
} else {
    initializeFileDetails();
}