import {post} from "../apiClient.js";
export async function downloadSingleFileApi({fileId,userId}){
    return post('/files/download/single', {fileId,userId});
}