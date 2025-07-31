import {post} from '../apiClient.js';

export async function signupUser({firstName,lastName,email,password,publicKey,encryptedPrivateKey,iv,walletAddress,signup_method}){
    return await post("/auth/signup",{
        firstName,
        lastName,
        email,
        password,
        publicKey,
        encryptedPrivateKey,
        iv,
        walletAddress,
        signup_method

    });
}
export async function loginUser({email,password}){
    return await post("/auth/login",{
        email,
        password
    });
}