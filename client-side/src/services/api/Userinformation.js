import {post} from "../apiClient.js";

export async function getUserPublicInformationApi(userId){
    return await post('/user/publicInformation', { userId }); // <-- key is userId (camelCase)
}


const respond = await getUserPublicInformationApi("51cf531f-8bc4-44a9-9bfd-1183cab10d47");
console.log(respond);
