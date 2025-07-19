package com.omvaultchain.storage.repository;

import com.omvaultchain.storage.model.UploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadStatusRepository extends JpaRepository<UploadStatus, String> {}
