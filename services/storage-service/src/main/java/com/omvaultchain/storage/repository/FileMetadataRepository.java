package com.omvaultchain.storage.repository;

import com.omvaultchain.storage.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, String> {
    Optional<FileMetadata> findByCid(String cid);
    List<FileMetadata> findAllByOwnerId(String ownerId);
}
