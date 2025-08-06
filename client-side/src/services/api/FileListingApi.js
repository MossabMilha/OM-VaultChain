import {post} from "../apiClient.js";
export async function listOwnedFileApi(ownerId){
    return post('/files/list/owned', {ownerId});
}
export async function listFileApi(userId){
    return post('/files/list', {userId});
}
export async function getFileMetadata(fileId ){
    return post('/files/metadata', {fileId });
}

