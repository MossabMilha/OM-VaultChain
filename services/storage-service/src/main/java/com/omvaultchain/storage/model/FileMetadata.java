package com.omvaultchain.storage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "files")
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fileName;
    private String mimeType;
    private long size;
    private String cid;
    private String fileHash;
    private String ownerWallet;
    private Instant uploadAt;
    private String status;// Currently We Have  : QUEUE,PROCESSING,COMPLETED,FAILED


}
