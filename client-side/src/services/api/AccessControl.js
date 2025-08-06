import {post} from "../apiClient.js";

export async function giveAccessSingleUserApi(fileId,userId,encryptedKey){
    return await post('/access/grantAccess', {fileId, userId,encryptedKey });
}



