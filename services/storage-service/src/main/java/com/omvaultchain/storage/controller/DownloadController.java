package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.DownloadBatchRequest;
import com.omvaultchain.storage.model.DownloadRequest;
import com.omvaultchain.storage.model.DownloadSingleFileResponse;
import com.omvaultchain.storage.model.StreamRequest;
import com.omvaultchain.storage.service.FileDownloadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/storage/download")
public class DownloadController {

    @Autowired
    private FileDownloadService fileDownloadService;

    /**
     * Downloads a file using its internal file ID.
     *
     * Request Body Example:
     * {
     *   "fileId": "9e34d3d5-2f1c-4c8b-95d9-ef85b17aabb2"
     * }
     *
     * @return Encrypted file content as a downloadable resource
     */
    @PostMapping("/single/id")
    public ResponseEntity<DownloadSingleFileResponse> downloadById(@RequestBody DownloadRequest request){
        DownloadSingleFileResponse response = fileDownloadService.downloadByFileId(request.getFileId());
        return ResponseEntity.ok(response);
    }

    /**
     * Downloads multiple files from storage using their file IDs.
     *
     * Request Body Example:
     * {
     *   "fileIds": [
     *     "fileId1",
     *     "fileId2",
     *     "fileId3"
     *   ]
     * }
     *
     * @return Encrypted combined file content as a downloadable resource (e.g., ZIP)
     */
    @GetMapping("/batch/id")
    public ResponseEntity<Resource> downloadBatchById(@RequestBody DownloadBatchRequest request){
        return fileDownloadService.downloadBatchByFileIds(request.getFileIds());
    }
    /**
     * Downloads multiple files from IPFS using their CIDs.
     *
     * Request Body Example:
     * {
     *   "CIDs": [
     *     "QmYwAPJzv5CZsnAztb8xueK8Nq8n5j9XAsr9LkUWjz6BCg",
     *     "QmZxg12f3gJkLt9vHGFd87FJKvCYaZmbPbZsLqU7x"
     *   ]
     * }
     *
     * @return Encrypted combined file content as a downloadable resource (e.g., ZIP)
     */
    @GetMapping("/batch/cid")
    public ResponseEntity<Resource> downloadBatchByCid(@RequestBody DownloadBatchRequest request){
        return fileDownloadService.downloadBatchByCIDs(request.getCIDs());
    }








}
