import {post} from "../apiClient.js";

export async function uploadSingleFileApi(payload){
    console.log(payload);
    return await post('/files/upload/single', payload);
}