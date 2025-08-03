import {post} from "../apiClient.js";
export async function downloadSingleFileApi({fileId,ownerId}){
    return post('/files/download/single', {fileId,ownerId});
}