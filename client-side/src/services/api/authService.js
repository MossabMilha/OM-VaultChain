import {post} from '../apiClient.js';

export async function signupUser({firstName,lastName,email,password,publicKey,encryptedPrivateKey,iv}){
    return await post("/auth/signup",{
        firstName,
        lastName,
        email,
        password,
        publicKey,
        encryptedPrivateKey,
        iv
    });
}