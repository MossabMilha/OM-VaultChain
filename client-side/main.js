import {encryptedFileEnvelope, encryptedFilesEnvelope} from "./src/crypto/createEnvelope.js";
import {decryptSingleFile, downloadDecryptedFile, verifyFileIntegrity} from "./src/crypto/decrypt.js";

const PublicKeyPem = `-----BEGIN PUBLIC KEY-----
    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvvRnANKhiaPPizdd8zkPnUPQabEvVOUgAn/pgDSVxkJRTmk0Snn6wFfgWYC7nlhwBZB6V1tJ4lA2Tgjhulzol321MvoQQTR+c4xIYtqrWhbG6rhiJwK5sk+RMNvshGtC0exRKqjfT/CGToSuP7nVQ8wL5cAB/tv7n2BWAH/PL1H2A0uwcn3Sn0xbjcmNIhQm99dOSn7xzljrX8vuuc3omNSYgWZCyBM0o9pGzCDNJ5QnwnxZL0PHRJ+H/LzDJ6FvbMYIZBgaP7LNFVPQBY5jrCVnDCD18XodTOTz7TjudQFalz6lpCeVtvwu9MfH3cNgYLJ/uZVlN1UIv7PBMV8EGQIDAQAB
-----END PUBLIC KEY-----`;
const PrivateKeyPem = `-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC+9GcA0qGJo8+LN13zOQ+dQ9BpsS9U5SACf+mANJXGQlFOaTRKefrAV+BZgLueWHAFkHpXW0niUDZOCOG6XOiXfbUy+hBBNH5zjEhi2qtaFsbquGInArmyT5Ew2+yEa0LR7FEqqN9P8IZOhK4/udVDzAvlwAH+2/ufYFYAf88vUfYDS7ByfdKfTFuNyY0iFCb3105KfvHOWOtfy+65zeiY1JiBZkLIEzSj2kbMIM0nlCfCfFkvQ8dEn4f8vMMnoW9sxghkGBo/ss0VU9AFjmOsJWcMIPXxeh1M5PPtOO51AVqXPqWkJ5W2/C70x8fdw2Bgsn+5lWU3VQi/s8ExXwQZAgMBAAECggEAE3mht7fDueH3HN+8uT2slgGAlAb6KNJlfVvGWMkELYA+Ap1ANAhUxqEvxHEYsYGbizW9BIPO+UHtB/Bn9TXckvbDMDvIxJ2x997q0woyR7Hi/7VmbUwd3E25JR6I9MhrvDU54fvvKpwBVqi5jM5LLer1m60FhxSwiQSuQMbIPq8UKnW/2TEKeA/ZoCtXivCXFio8uMa4FVrOBgvZzvVYJ0dNV+dldt+hfva2ZFMtlj9m9XL2RaxPrr2IJBj/4s+qGVASugPrxXqhyXgBUcseDf1C7ztp79/O4gzS6CjuHRGqxEMdWneOZNPA+6aR3C/K5xr3Ga+f/IjLHD4pvEc+8QKBgQDegiyDX7EiBMeekXOUACLUqZWL/2fWoWIsq2BGjsQUlIy9+0P0Xegb5h8IN7aSB0YwaT4NEmJEAF/ig3ue6q8t9KII6TrcqS2nr0gWSocO2BSuZ2AmfWPMzX3K636taZ+wXcX26i2dOTNyPIDd+Q56ltQ9n+VbI/USaFPdEBW/gwKBgQDbsmEeGajaieJHoejnlIhM/cZ5aDhRZ/00Bk/Hnrz0eLwcH+uPl3leWTkf0rU5/8m0+swsOPiYI2q9KFH+jsXyEsqG1In7z5KAs/Tx4v9e5bWvIEwGoiJP0E3/mshC9aKg9WC4n75+1aO/CmkFDsYXUJ03We2P98TIV1Nn8XMfMwKBgHOivRVtRrYCO+e5eZJh27gfhdui2Ukex9GlIryod1imttoI2hEGOJk/J8NyiMwtQZnpJ9lNQ4pJk4s5f6ZgGrhMWWU+KFwDA/oQSutiwfvB6kWU7dAy/Am0hXpOlp/ys43JvhcLLrd1wbnNnb8+LvsgpZ59GYobDsMJOwQ41sSXAoGBANeX+FgqhBpZvzXefIltqEgtreyMSuAQNVktbHMPoowiQy8yX97cndzpn/ZLt06AZjf0+hHXopTiBKg1/ijZgQTqTdb8kaAL9c/ZSWFqCoLc12tRPWoxB4zddP2FrkqPdJWR1uW21aDwFC2wkJjHA7vzfyTHWiqy0Whs0cuqFPgjAoGBAMTBT1RTu6F8xNQhjKVCgnFFWM82LioPxs53B3xueTWCQNLx/cMNdWDA4RcG64DD7tNl41ZuEugUOraEMCJ7IPPHf0HMpQOpi+ehmmxB27cAbSl4z3iS1Pf4rCklf7HroxUBo3a7SY937dCyb6mZXq8mT+J3oIRQRC5sI2lxaL8F
-----END PRIVATE KEY-----`;

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
    try{
        const response = await fetch('http://127.0.0.1:8000/api/files/download/single', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({fileId: FileId, ownerId: ownerId})
        })
        if (!response.ok) {
            const error = await response.json();
            console.log('Error : ', error);
        }
        const encryptedEnvelope = await response.json();
        console.log('Success:', encryptedEnvelope);

        const decryptedFile = await decryptSingleFile(encryptedEnvelope, PrivateKeyPem);

        const isValid = await verifyFileIntegrity(decryptedFile.blob, decryptedFile.fileHash);
        if (!isValid) {
            throw new Error('File Integrity Check Failed');
        }
        downloadDecryptedFile(decryptedFile);
    }catch (error) {
        console.error('Decryption Error : ', error);
        alert("Decryption Failed: " + error.message);
    }
}

document.getElementById('upload').addEventListener('click', async() => {
    const selectedFile = document.getElementById('file').files[0];

    if (!selectedFile) {
        alert('Please select at least one file to upload.');
        return;
    }
    await uploadEncryptedFile(selectedFile, PublicKeyPem, "123e4567-e89b-12d3-a456-426614174000");

})
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
    await downloadSingleFile("ba57a3e5-e587-4051-8e97-a1abb7df21d1", "123e4567-e89b-12d3-a456-426614174000");
})










