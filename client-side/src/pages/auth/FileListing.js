import { listFiles } from "../../utils/FileListing.js";
import { downloadSingleFile } from "../../utils/DownloadFile.js";
import { searchUsersApi } from "../../services/api/Userinformation.js";
import {giveAccessSingleUser} from "../../utils/ControlAccess.js";

let currentFileIdToShare = null;

let searchTimeout = null;
const usernameInput = document.getElementById("username");
const suggestionList = document.getElementById("userSuggestions");



function openShareModel(fileId) {
    currentFileIdToShare = fileId;
    document.getElementById("shareModal").style.display = "block";
    document.getElementById("modalOverlay").style.display = "block";
    document.getElementById("username").value = "";
}
function closeShareModel() {
    currentFileIdToShare = null;
    document.getElementById("shareModal").style.display = "none";
    document.getElementById("modalOverlay").style.display = "none";
    document.getElementById("username").value = "";
}

function getFileIcon(mime) {
    if (mime.startsWith("image/")) return "🖼️";
    if (mime.startsWith("application/pdf")) return "📄";
    return "📁";
}
function displayFiles(files) {
    const fileListContainer = document.getElementById("fileList");
    fileListContainer.innerHTML = "";
    files.forEach(file => {
        const fileItem = document.createElement("div");
        fileItem.className = "fileItem";

        const icon = document.createElement("div");
        icon.className = "icon";
        icon.textContent = getFileIcon(file.mime_type);

        const name = document.createElement("div");
        name.className = "name";
        name.textContent = file.name;
        name.style.cursor = "pointer";
        name.addEventListener("click", () => {
            window.location.href = `FileDetails.html?id=${file.id}`;
        });

        const downloadBtn = document.createElement("button");
        downloadBtn.innerHTML = "⬇️";
        downloadBtn.title = "Download";
        downloadBtn.onclick = () => downloadSingleFile(file.id);

        if (file.accessType === "owned") {
            const viewBtn = document.createElement("button");
            viewBtn.innerHTML = "🔗";
            viewBtn.title = "Share Access";
            viewBtn.onclick = () => openShareModel(file.id);
            fileItem.append(icon, name, viewBtn, downloadBtn);
        } else {
            fileItem.append(icon, name, downloadBtn);
        }

        fileListContainer.appendChild(fileItem);
    });
}
function displaySuggestions(users) {
    suggestionList.innerHTML = "";
    users.forEach(user => {
        const li = document.createElement("li");
        li.textContent = user.username;
        li.addEventListener("click", () => {
            usernameInput.value = user.username;
            usernameInput.dataset.userId = user.id; // store ID
            suggestionList.innerHTML = "";
        });
        suggestionList.appendChild(li);
    });
}


document.getElementById("uploadButton").addEventListener("click", () => {
    window.location.href = "UploadFiles.html";
});
document.getElementById("ConfirmShareBtn").addEventListener("click", async () => {
    const userId = usernameInput.dataset.userId;
    if (!userId || !currentFileIdToShare) {
        alert("Please select a valid user.");
        return;
    }
    try{
        console.log(currentFileIdToShare)
        console.log(userId)
        const respond = await giveAccessSingleUser(userId,currentFileIdToShare);
        console.log("Share response:", respond);
        alert("Access shared successfully!");
        closeShareModel();
    }catch (error) {
        console.error("Error sharing access:", error);
        alert("Failed to share access.");
    }
});
document.getElementById("CancelShareBtn").addEventListener("click", closeShareModel);
document.getElementById("modalOverlay").addEventListener("click", closeShareModel);
document.addEventListener("DOMContentLoaded", async () => {
    try {
        const files = await listFiles();
        console.log(files);
        displayFiles(files);
    } catch (error) {
        console.log(error);
    }
});

usernameInput.addEventListener("input", () => {
    clearTimeout(searchTimeout);
    const query = usernameInput.value.trim();
    if (!query) {
        suggestionList.innerHTML = "";
        return;
    }
    searchTimeout = setTimeout(async () => {
        try {
            const users = await searchUsersApi(query); // ✅ await now inside async
            displaySuggestions(users);
        } catch (error) {
            console.error("Search error:", error);
        }
    }, 300);
});


