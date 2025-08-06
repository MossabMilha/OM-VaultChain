import {getCurrentUser} from "./userKeyStorage.js";
import {listOwnedFileApi,listFileApi} from "../services/api/FileListingApi.js";


export async function listOwnedFiles() {
    try {
        const currentUser = getCurrentUser();

        if (!currentUser?.userId) {
            throw new Error("User credentials not found. Please log in again.");
        }
        const response = await listOwnedFileApi(currentUser.userId);

        if (!response || !Array.isArray(response.files)) {
            throw new Error("Invalid response format from server.");
        }

        return response.files;
    } catch (error) {

        throw new Error(
            error.message || "Failed to fetch owned files. Please try again later."
        );
    }
}
export async function listFiles(){
    try {
        const currentUser = getCurrentUser();

        if (!currentUser?.userId) {
            throw new Error("User credentials not found. Please log in again.");
        }
        const response = await listFileApi(currentUser.userId);

        if (!response || !Array.isArray(response.files)) {
            throw new Error("Invalid response format from server.");
        }

        return response.files;
    } catch (error) {

        throw new Error(
            error.message || "Failed to fetch owned files. Please try again later."
        );
    }
}


