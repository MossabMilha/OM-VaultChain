import {uploadSingleFile,uploadBatchFile} from "../../utils/UploadFiles.js";



document.getElementById('uploadForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const fileInput = document.getElementById('file');
    const files = fileInput.files;
    if (!fileInput.files.length) {
        alert('Please select a file to upload');
        return;
    }
    if (files.length === 1) {
        try{
            const respond = await uploadSingleFile(files[0]);
            console.log(respond);
        }catch (error) {
            console.log(error);
        }
    } else {
        const respond = await uploadBatchFile(files);
        console.log(respond);
    }


});