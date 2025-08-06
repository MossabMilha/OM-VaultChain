# Storage Service

## What This Service Does

This is a **decentralized file storage microservice** that handles encrypted file uploads, downloads, and access management. It stores files on IPFS (decentralized storage) while managing metadata and permissions in a database.

### Core Purpose
- Store encrypted files securely on IPFS network
- Manage file metadata and access permissions
- Handle large file uploads with resumable capability
- Provide secure file sharing between users
- Track all access through blockchain-style audit trails

## Service Architecture

### Main Components

**Controllers**
- `UploadController` - Handles file upload operations
- `DownloadController` - Manages file downloads and streaming

**Core Services**
- `FileUploadService` - Processes uploads and saves metadata
- `FileDownloadService` - Retrieves files and handles batch downloads
- `FileSearchService` - Searches files by metadata criteria
- `IPFSClient` - Communicates with IPFS network via Pinata

**Data Storage**
- `FileMetadata` - File information and encryption details
- `AccessPermission` - User access rights with audit tracking
- `UploadStatus` - Progress tracking for large file uploads

## What The Service Can Do

### File Upload Operations

**Single File Upload**
- Takes encrypted files (Base64 encoded)
- Stores them on IPFS network
- Saves metadata in database
- Returns unique file ID and IPFS address (CID)

**Batch Upload**
- Uploads multiple files at once
- Processes each file individually
- Returns results for all files

**Resumable Upload (for large files)**
- Splits large files into chunks
- Tracks upload progress
- Can resume if interrupted
- Handles files up to 500MB

### File Download Operations

**Single File Download**
- Checks if user has permission
- Gets file from IPFS using its CID
- Returns encrypted file with decryption info

**Batch Download**
- Downloads multiple files
- Packages them into a ZIP file
- Returns the ZIP archive

**File Streaming**
- Streams large files directly from IPFS
- Good for videos or large documents
- Memory efficient

### File Search

**Search Capabilities**
- Find files by name (partial matches)
- Filter by file type (PDF, image, etc.)
- Filter by file size or upload date
- Search only your own files
- Paginated results with sorting

### Access Control

**Permission System**
- File owners control who can access their files
- Share files by giving encrypted keys to other users
- Track all access attempts with blockchain-style records
- Can revoke access anytime

**Security Features**
- All files encrypted with AES-256-GCM
- Each file has its own encryption key
- Files verified with SHA-256 hashes
- Encryption happens before upload

## How It Works Internally

### IPFS Storage
- Uses Pinata service to store files on IPFS network
- IPFS gives each file a unique CID (Content Identifier)
- Files are immutable - can't be changed once uploaded
- Identical files get the same CID (deduplication)

### Database Storage
The service uses MySQL to store:

**File Information (`FileMetadata`)**
- File name, size, type, owner
- IPFS CID where file is stored
- Encryption details (IV, encrypted key, hash)
- Upload timestamps

**Access Permissions (`AccessPermission`)**
- Who can access which files
- Encrypted keys for each user
- Blockchain transaction hashes for audit
- When access was granted/revoked

**Upload Progress (`UploadStatus`)**
- Tracks large file uploads
- Stores how much has been uploaded
- Manages temporary file chunks

## File Processing Flow

### When You Upload a File
1. Service receives your encrypted file (Base64 format)
2. Extracts file info (name, size, type, hash)
3. Uploads encrypted file to IPFS via Pinata
4. IPFS gives back a CID (unique file address)
5. Saves file info and CID to database
6. Returns file ID and CID to you

### When You Download a File
1. You request a file by its ID
2. Service checks if you have permission
3. Gets file info from database
4. Downloads encrypted file from IPFS using CID
5. Returns encrypted file with decryption details
6. You decrypt the file on your end

### When You Share a File
1. You grant access to another user
2. Service encrypts the file's key for that user
3. Creates permission record with audit info
4. Other user can now download and decrypt the file

## Key Capabilities

**Security**
- All files encrypted before storage
- Each file has unique encryption key
- File integrity verified with hashes
- Complete audit trail of all access

**Performance**
- Handles files up to 500MB
- Resumable uploads for large files
- Batch operations for multiple files
- Efficient streaming for large downloads

**Reliability**
- Files stored on decentralized IPFS network
- Professional Pinata service ensures availability
- Automatic error handling and recovery
- Progress tracking for long uploads
