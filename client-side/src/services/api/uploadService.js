import {post} from "../apiClient.js";

export async function uploadSingleFileApi(payload){
    return await post('/files/upload/single', payload);
}