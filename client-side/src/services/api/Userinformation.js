import {post} from "../apiClient.js";

export async function getUserPublicInformationApi(userId){
    return await post('/user/publicInformation', { userId });
}
export async function searchUsersApi(username) {
    return await post('/user/search', { username });
}



