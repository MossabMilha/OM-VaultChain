package com.omvaultchain.storage.repository;

import com.omvaultchain.storage.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, String>, JpaSpecificationExecutor<FileMetadata> {
    Optional<FileMetadata> findByCid(String cid);
    List<FileMetadata> findAllByOwnerId(String ownerId);
    List<FileMetadata> findByOwnerIdAndDeletedFalse(String ownerId);
}
