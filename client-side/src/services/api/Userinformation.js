import {post} from "../apiClient.js";

export async function getUserPublicInformationApi(userId){
    return await post('/user/publicInformation', { userId }); // <-- key is userId (camelCase)
}



