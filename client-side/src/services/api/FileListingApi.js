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
export async function getCurrentVersionFileApi(userId,fileId){
    return post('/files/currentVersion', {userId,fileId});
}
export async function getAllVersionFileApi(userId,fileId){
    return post('/files/allVersions', {userId,fileId});
}
