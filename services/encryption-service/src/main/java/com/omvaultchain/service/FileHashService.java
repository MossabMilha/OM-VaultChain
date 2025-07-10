package com.omvaultchain.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
/**
 * (X_X) was here
 * FileHashService
 *
 * This service is responsible for computing a cryptographic fingerprint (hash) of a file
 * using the SHA-256 algorithm. It is used to ensure file integrity, support version tracking,
 * detect tampering, and uniquely identify files within the OM VaultChain system.
 *
 * Usage Scenarios:
 * - When a file is uploaded:
 *   → The system computes the SHA-256 hash of the raw file content (before encryption).
 *   → This hash is stored in the metadata and registered on-chain to anchor the file's integrity.
 *
 * - When a file is retrieved:
 *   → The system recomputes the hash from the file.
 *   → It verifies the computed hash matches the one stored — ensuring the file wasn't corrupted or tampered with.
 *
 * Methods:
 * - computeSHA256(byte[]): Computes hash for in-memory data (e.g., files just received).
 * - computeSHA256(Path): Computes hash for files stored on disk (e.g., temp files).
 * - bytesToHex(byte[]): Converts a binary hash to a human-readable hexadecimal string.
 *
 * Notes:
 * - The returned hash is a 64-character lowercase hexadecimal SHA-256 digest.
 * - This class is stateless and thread-safe.
 *
 */
@Service
public class FileHashService {
    public String computeSHA256(byte[] Data)throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(Data);
        return bytesToHex(hash);
    }

    public String computeSHA256(Path FilePath) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int  read;
        try (InputStream is = Files.newInputStream(FilePath)) {
            while ((read = is.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        return bytesToHex(digest.digest());
    }

    public String bytesToHex(byte[] hash) {
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            int v = b & 0xFF; // unsigned conversion
            if (v < 16) hex.append('0');
            hex.append(Integer.toHexString(v));
        }
        return hex.toString();
    }
}
