import {post} from '../apiClient.js';

export async function signupUserBackupCode({firstName,lastName,email,password,publicKey,encryptedPrivateKey,iv}){
    return await post("/auth/signup/backupCode",{
        firstName,
        lastName,
        email,
        password,
        publicKey,
        encryptedPrivateKey,
        iv
    });
}