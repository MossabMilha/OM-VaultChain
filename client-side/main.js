import {encryptedFileEnvelope, encryptedFilesEnvelope} from "./src/crypto/createEnvelope.js";

const PublicKeyPem = `-----BEGIN PUBLIC KEY-----
    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvvRnANKhiaPPizdd8zkPnUPQabEvVOUgAn/pgDSVxkJRTmk0Snn6wFfgWYC7nlhwBZB6V1tJ4lA2Tgjhulzol321MvoQQTR+c4xIYtqrWhbG6rhiJwK5sk+RMNvshGtC0exRKqjfT/CGToSuP7nVQ8wL5cAB/tv7n2BWAH/PL1H2A0uwcn3Sn0xbjcmNIhQm99dOSn7xzljrX8vuuc3omNSYgWZCyBM0o9pGzCDNJ5QnwnxZL0PHRJ+H/LzDJ6FvbMYIZBgaP7LNFVPQBY5jrCVnDCD18XodTOTz7TjudQFalz6lpCeVtvwu9MfH3cNgYLJ/uZVlN1UIv7PBMV8EGQIDAQAB
-----END PUBLIC KEY-----`;


async function uploadEncryptedFile(originalFile, recipientPublicKeyPem, ownerId) {
    try {
        const envelope = await encryptedFileEnvelope(originalFile, recipientPublicKeyPem, ownerId);
        console.log(envelope);
        
        const response = await fetch('http://127.0.0.1:8000/api/files/upload/single', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(envelope),
        });


        if(!response.ok){
            const error = await response.json();
            console.log('Error : ', error);
        }

        const result = await response.json();

        console.log('Success:', result);

    } catch (error) {
        console.error('Error uploading encrypted file:', error);
        throw error;
    }
}

async function uploadEncryptedBatch(originalFiles, recipientPublicKeyPem, ownerId) {
    try{
        const batchEnvelope = await encryptedFilesEnvelope(originalFiles, recipientPublicKeyPem, ownerId);

        const response = await fetch('http://127.0.0.1:8000/api/files/upload/batch', {
            method: 'POST',
            headers:{ 'Content-Type': 'application/json'},
            body: JSON.stringify(batchEnvelope)
        });
        if(!response.ok){
            const error = await response.json();
            console.log('Error : ', error);
        }
        const responseData = await response.json();
        console.log('Batch upload response:', responseData);

    }catch(error){
        console.error('Error uploading encrypted batch:', error);
        throw error;
    }

}

async function listFiles(ownerId) {
    try{
        const respond = await fetch('http://127.0.0.1:8000/api/files/list', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ ownerId: ownerId })
        });
        if(!respond.ok){
            const error = await respond.json();
            console.log('Error : ', error);
        }
        const files = await respond.json();
        console.log('Files:', files);
    }catch(error){
        console.error('Error listing files:', error);
        throw error;
    }
}

async function downloadSingleFile(FileId,ownerId){
    const response = await fetch('http://127.0.0.1:8000/api/files/download/single', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ fileId: FileId, ownerId: ownerId })
    })
    if(!response.ok){
        const error = await response.json();
        console.log('Error : ', error);
    }
    const fileData = await response.json();
    console.log('Success:', fileData);
}

// document.getElementById('upload').addEventListener('click', async() => {
//     const selectedFile = document.getElementById('file').files[0];
//
//     if (!selectedFile) {
//         alert('Please select at least one file to upload.');
//         return;
//     }
//     await uploadEncryptedFile(selectedFile, PublicKeyPem, "123e4567-e89b-12d3-a456-426614174000");
//
// })
// document.getElementById('upload').addEventListener('click', async() => {
//     const input = document.getElementById('file');
//     const selectedFiles = Array.from(input.files)
//     if (!selectedFiles.length ) {
//         alert('Please select at least one file to upload.');
//         return;
//     }
//     await uploadEncryptedBatch(selectedFiles, PublicKeyPem, "123e4567-e89b-12d3-a456-426614174000");
// })

document.getElementById('list').addEventListener('click', async() => {
    await listFiles("123e4567-e89b-12d3-a456-426614174000");
})
document.getElementById('download').addEventListener('click', async() => {

})










