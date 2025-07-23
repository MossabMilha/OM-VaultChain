import {encryptedFileEnvelope} from "./src/crypto/createEnvelope.js";

const PublicKeyPem = `-----BEGIN PUBLIC KEY-----
    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvvRnANKhiaPPizdd8zkPnUPQabEvVOUgAn/pgDSVxkJRTmk0Snn6wFfgWYC7nlhwBZB6V1tJ4lA2Tgjhulzol321MvoQQTR+c4xIYtqrWhbG6rhiJwK5sk+RMNvshGtC0exRKqjfT/CGToSuP7nVQ8wL5cAB/tv7n2BWAH/PL1H2A0uwcn3Sn0xbjcmNIhQm99dOSn7xzljrX8vuuc3omNSYgWZCyBM0o9pGzCDNJ5QnwnxZL0PHRJ+H/LzDJ6FvbMYIZBgaP7LNFVPQBY5jrCVnDCD18XodTOTz7TjudQFalz6lpCeVtvwu9MfH3cNgYLJ/uZVlN1UIv7PBMV8EGQIDAQAB
-----END PUBLIC KEY-----`;



document.getElementById('upload').addEventListener('click', async() => {
    const selectedFile = document.getElementById('file').files[0];
    if (!selectedFile) {
        alert('Please select a file to upload.');
        return;
    }
    try{
        const ownerId = "123e4567-e89b-12d3-a456-426614174000"
        const {envelope} = await encryptedFileEnvelope(selectedFile,PublicKeyPem,ownerId);
        const response = await fetch('http://localhost:8080/storage/upload/single', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(envelope)
        });
        if(!response.ok){
            const error = await response.json();
            console.log('Error : ', error);
        }
        const result = await response.json();
        console.log('Success:', result);

    }catch (error) {
        console.log(error);
    }

});