package com.omvaultchain.storage.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "files")
@Data
public class FileMetadata {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "owner_id", nullable = false, columnDefinition = "CHAR(36)")
    private String ownerId;

    @Column(name = "name")
    private String fileName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "cid", unique = true, nullable = false)
    private String cid;

    @Column(name = "file_hash", nullable = false)
    private String fileHash;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;


}
