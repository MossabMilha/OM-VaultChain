package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import com.omvaultchain.storage.model.LimitedInputStream;
import com.omvaultchain.storage.repository.FileMetadataRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileDownloadService {
    private final FileMetadataRepository fileMetadataRepository;
    private final IPFSClient IPFSClient;

    private ResponseEntity<Resource> prepareDownloadResponse(byte[] data, String name, String mime) {
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mime))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .body(resource);
    }
    public ResponseEntity<Resource> downloadByFileId(String fileId){
        FileMetadata metadata = fileMetadataRepository.findById(fileId).orElseThrow(
                ()->new RuntimeException("File Not Found"));
        byte[] content = IPFSClient.downloadFile(metadata.getCid());
        return prepareDownloadResponse(content,metadata.getFileName(), metadata.getMimeType());

    }
    public ResponseEntity<Resource> downloadByCid(String cid){
        Optional<FileMetadata> optionalMetadata = fileMetadataRepository.findByCid(cid);
        if (optionalMetadata.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        FileMetadata metadata = optionalMetadata.get();
        byte[] content = IPFSClient.downloadFile(cid);
        return prepareDownloadResponse(content, metadata.getFileName(), metadata.getMimeType());

    }
    public ResponseEntity<Resource> downloadBatchByFileIds(List<String> fileIds){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            for (String fileId : fileIds){
                Optional<FileMetadata> optionalMetadata = fileMetadataRepository.findById(fileId);
                if (optionalMetadata.isEmpty()) continue;
                FileMetadata metadata = optionalMetadata.get();

                byte[] data = IPFSClient.downloadFile(metadata.getCid());
                if (data == null) continue;

                ZipEntry entry = new ZipEntry(metadata.getFileName());
                zos.putNextEntry(entry);
                zos.write(data);
                zos.closeEntry();
            }
            zos.close();
            ByteArrayResource zipResource = new ByteArrayResource(baos.toByteArray());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
                    .body(zipResource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    public ResponseEntity<Resource> downloadBatchByCIDs(List<String> CIDs){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            for (String CID : CIDs){
                Optional<FileMetadata> optionalMetadata = fileMetadataRepository.findByCid(CID);
                if (optionalMetadata.isEmpty()) continue;
                FileMetadata metadata = optionalMetadata.get();

                byte[] data = IPFSClient.downloadFile(metadata.getCid());
                if (data == null) continue;

                ZipEntry entry = new ZipEntry(metadata.getFileName());
                zos.putNextEntry(entry);
                zos.write(data);
                zos.closeEntry();
            }
            zos.close();
            ByteArrayResource zipResource = new ByteArrayResource(baos.toByteArray());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
                    .body(zipResource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    public ResponseEntity<Resource> streamFileById(String fileId, HttpServletRequest request){
        Optional<FileMetadata> metadataOpt  = fileMetadataRepository.findById(fileId);
        if(metadataOpt.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        FileMetadata metadata = metadataOpt.get();
        InputStream fileStream = IPFSClient.streamFile(metadata.getCid());
        String range = request.getHeader(HttpHeaders.RANGE);
        long fileSize = metadata.getSizeBytes();
        try {
            if(range != null && range.startsWith("bytes=")){
                long start,end;
                String[] parts = range.replace("bytes=", "").split("-");
                start = Long.parseLong(parts[0]);
                end = (parts.length > 1 && !parts[1].isEmpty()) ? Long.parseLong(parts[1]) : fileSize-1;

                long contentLength = end - start + 1;
                fileStream.skip(start);

                InputStream partialStream = new LimitedInputStream(fileStream, contentLength);
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .header(HttpHeaders.CONTENT_TYPE,metadata.getMimeType())
                        .header(HttpHeaders.CONTENT_LENGTH,String.valueOf(contentLength))
                        .header(HttpHeaders.CONTENT_RANGE,"bytes "+ start+"-"+end+"/"+fileSize)
                        .body(new InputStreamResource(partialStream));
            }else {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, metadata.getMimeType())
                        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize))
                        .body(new InputStreamResource(fileStream));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
