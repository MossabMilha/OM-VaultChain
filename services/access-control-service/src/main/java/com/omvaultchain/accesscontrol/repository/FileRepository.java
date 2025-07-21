package com.omvaultchain.accesscontrol.repository;

import com.omvaultchain.accesscontrol.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File,String> {
    @Query("SELECT f.ownerId FROM File f WHERE f.id = :fileId")
    Optional<String> findOwnerIdByFileId(@Param("fileId") String fileId);
}
