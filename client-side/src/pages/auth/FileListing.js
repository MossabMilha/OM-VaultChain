import {listFiles} from "../../utils/FileListing.js";
import {downloadSingleFile} from "../../utils/DownloadFile.js";

document.addEventListener("DOMContentLoaded", async () => {
    try{
        const files = await listFiles();
        console.log(files);
        displayFiles(files);
    }catch (error) {
        console.log(error); // Made Smth here Later
    }
})


function getFileIcon(mime){
    if(mime.startsWith("image/")) return "🖼️";
    if(mime.startsWith("application/pdf")) return "📄";
    return "📁";
}

function displayFiles(files){
    const fileListContainer = document.getElementById("fileList");
    fileListContainer.innerHTML = "";
    files.forEach(file =>{
        const fileItem = document.createElement("div");
        fileItem.className = "fileItem";

        const icon = document.createElement("div");
        icon.className = "icon";
        icon.textContent = getFileIcon(file.mime_type);

        const name = document.createElement("div");
        name.className = "name";
        name.textContent = file.name;


        const downloadBtn = document.createElement("button");
        downloadBtn.innerHTML = "⬇️";
        downloadBtn.title = "Download";
        downloadBtn.onclick = () => downloadSingleFile(file.id);

        if (file.accessType === "owned" ) {
            const viewBtn = document.createElement("button");
            viewBtn.innerHTML = "👁️";
            viewBtn.title = "View";
            viewBtn.onclick = () => console.log(`View ${file.id}`);

            fileItem.append(icon, name, viewBtn, downloadBtn);
        }else {
            fileItem.append(icon, name, downloadBtn);
        }


        fileListContainer.appendChild(fileItem);
    })
}

document.getElementById("uploadButton").addEventListener("click", () => {
    window.location.href = "UploadFiles.html";
});

