# 🔐 OM VaultChain - Technical Specification

> **Decentralized File Storage Platform with Blockchain Access Control**

A comprehensive SaaS platform for encrypted file storage utilizing client-side encryption, blockchain (Ethereum/Polygon) for access control, and IPFS for decentralized storage.

---

## 📋 Table of Contents

- [🎯 Project Overview](#-project-overview)
- [🛠️ Technology Stack](#️-technology-stack)
- [🏗️ System Architecture](#️-system-architecture)
- [🖥️ Frontend Layer](#️-frontend-layer)
- [🌐 API Gateway](#-api-gateway)
- [🔧 Backend Microservices](#-backend-microservices)
- [📊 Database Schema](#-database-schema)
- [🚀 Development Guidelines](#-development-guidelines)

---

## 🎯 Project Overview

### Context
OM VaultChain provides secure, decentralized file storage with immutable access control through blockchain technology and client-side encryption.

### Objectives
- ✅ Enable encrypted file uploads with complete access control
- ✅ Provide revocable, traceable, transparent, and tamper-proof access system
- ✅ Deliver secure, scalable infrastructure compliant with privacy requirements

### Target Audience
- 🏢 **Enterprises** handling sensitive data
- ⚖️ **Legal professionals** (lawyers, accountants)
- 👤 **Individuals** protecting personal documents
- 🛡️ **Cybersecurity companies**

---

## 🛠️ Technology Stack

| Layer | Technology | Purpose |
|-------|------------|---------|
| 🔐 **Encryption** | Java + BouncyCastle (AES-256-GCM, RSA/ECIES) | Client-side encryption |
| 📦 **Storage** | IPFS with Pinata | Decentralized file storage |
| ⛓️ **Smart Contracts** | Solidity on Polygon + Hardhat | Access control & metadata |
| 🔗 **Blockchain SDK** | web3j (Java) | Smart contract interaction |
| 🧩 **Backend Coordination** | Spring Boot | Microservices orchestration |
| 📊 **Metadata Format** | Custom JSON structure | File metadata management |
| 👛 **Wallet/Auth** | MetaMask, WalletConnect | User authentication |
| 🖥️ **Frontend** | React (web) or Flutter (mobile) | User interface |
| 🌐 **API Gateway** | Spring Cloud Gateway | Service coordination |
| 💾 **Database** | MySQL 8.0 + Redis | Metadata & caching |

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Frontend Layer                          │
├─────────────────────┬─────────────────────┬─────────────────┤
│   Front Office App  │   Back Office App   │  Auth Interface │
│   (User Platform)   │   (Admin Portal)    │  (Wallet Auth)  │
└─────────────────────┴─────────────────────┴─────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                     API Gateway                             │
│              Spring Cloud Gateway                           │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                   Backend Microservices                     │
├─────────────┬─────────────┬─────────────┬───────────────────┤
│auth-service │encryption-  │storage-     │blockchain-        │
│             │service      │service      │service            │
│             │             │             │                   │
│             │             │             │                   │
├─────────────┴─────────────┴─────────────┴───────────────────┤
│access-control-service     │ metadata-service                │
│                          │                                  │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                  External Services                          │
├─────────────────────┬─────────────────────┬─────────────────┤
│       IPFS          │    Blockchain       │    Database     │
│   (Pinata/Web3)     │  (Polygon Network)  │    (MySQL)      │
└─────────────────────┴─────────────────────┴─────────────────┘
```

---

## 🖥️ Frontend Layer

### 🎨 Front Office App (User Platform)
**Technology:** React.js + ethers.js + Tailwind CSS

#### Core Components:

**📊 Personal Dashboard**
- File list with sorting (date, size, name, owner)
- Shared files view
- Storage usage analytics
- Quick actions panel

**🔒 Secure Upload Module**
- Local file selection
- Client-side encryption (AES-256-GCM)
- IPFS upload with progress tracking
- User access management
- Blockchain registration

**🔑 Sharing & Access Management**
- Access rights viewer
- Add/remove users (by wallet address)
- Permission history
- Bulk sharing options

**📁 File Viewer/Download**
- Encrypted file retrieval from IPFS
- AES key decryption with wallet
- Local file decryption
- Preview for supported formats

**🕐 Version History**
- Version timeline
- Download previous versions
- Version comparison
- Restore capabilities

**👤 User Profile**
- Wallet address display
- Public key management
- Account settings
- Data export/deletion

### 🔧 Back Office App (Admin Portal)
**Technology:** React.js + Chart.js + Material-UI

#### Admin Components:

**📊 Global Analytics**
- Platform usage metrics
- Storage volume tracking
- User activity monitoring
- Revenue analytics

**🔍 Audit & History**
- Access logs
- Action history
- User behavior tracking
- Security incident reports

**👥 User Management**
- User account overview
- Permission reset
- Account suspension
- Support ticket management

**📡 IPFS Supervision**
- File availability monitoring
- CID validation
- Pinning status
- Storage health checks

**🚨 Incident Management**
- Abuse reporting
- Content moderation
- User notifications
- Access revocation

**💳 Billing & Plans**
- Subscription management
- Usage tracking
- Payment processing
- Plan upgrades

### 🔐 Authentication Interface
**Technology:** ethers.js + WalletConnect

**🔑 Wallet Connection**
- MetaMask integration
- WalletConnect support
- Challenge generation
- Signature verification
- Session management

---

## 🌐 API Gateway

**Technology:** Spring Cloud Gateway + Spring Security

### Core Functions:
- **Authentication & Session Management**
- **Request Routing & Load Balancing**
- **Rate Limiting & Security**
- **API Documentation & Monitoring**

### API Endpoints:

#### 🔐 Authentication
- `POST /auth/challenge` - Generate wallet challenge
- `POST /auth/verify` - Verify signature & create session
- `POST /auth/refresh` - Refresh JWT token
- `DELETE /auth/logout` - Terminate session

#### 📁 File Operations
- `POST /files/upload` - Upload encrypted file
- `GET /files/{id}` - Download file
- `GET /files/list` - List user files
- `DELETE /files/{id}` - Delete file

#### 🎯 Access Control
- `POST /access/grant` - Grant file access
- `POST /access/revoke` - Revoke access
- `GET /access/{fileId}` - Get access list

#### 📚 Version Management
- `POST /files/{id}/versions` - Upload new version
- `GET /files/{id}/versions` - Get version history

#### 🧾 Metadata
- `GET /metadata/{fileId}` - Get file metadata
- `PUT /metadata/{fileId}` - Update metadata

---

## 🔧 Backend Microservices

### 🔐 auth-service
**Technology:** Spring Boot + Spring Security + JWT + web3j

#### Internal Components:

**🎲 ChallengeManager**
```java
@Component
public class ChallengeManager {
    // Generate unique challenge per wallet
    // Store in Redis with 5-minute expiration
    // Challenge validation
}
```

**✍️ SignatureVerifier**
```java
@Service
public class SignatureVerifier {
    // EIP-191 signature verification
    // Address extraction from signature
    // Wallet ownership validation
}
```

**🎫 JWTTokenService**
```java
@Service
public class JWTTokenService {
    // JWT token generation
    // Token validation
    // Claims management (wallet, role, expiration)
}
```

**👤 UserRegistry**
```java
@Repository
public class UserRegistry {
    // Wallet → user metadata mapping
    // Public key registration
    // SaaS profile management
}
```

#### Project Structure:
```
auth-service/
├── src/main/java/com/omvaultchain/auth/
│   ├── controller/AuthController.java
│   ├── service/
│   │   ├── ChallengeManager.java
│   │   ├── SignatureVerifier.java
│   │   ├── JWTTokenService.java
│   │   └── UserRegistry.java
│   ├── model/
│   │   ├── AuthRequest.java
│   │   ├── AuthResponse.java
│   │   └── UserProfile.java
│   └── config/SecurityConfig.java
├── Dockerfile
└── pom.xml
```

---

### 🔐 encryption-service
**Technology:** Spring Boot + BouncyCastle + AES-256-GCM

#### Internal Components:

**🔒 AESService**
```java
@Service
public class AESService {
    // AES-256-GCM encryption/decryption
    // Key generation (256-bit)
    // IV management (12 bytes)
    // Tag verification
}
```

**🔑 AsymmetricEncryptionService**
```java
@Service
public class AsymmetricEncryptionService {
    // RSA/ECIES key encryption
    // Public key validation
    // AES key wrapping/unwrapping
}
```

**🔍 FileHashService**
```java
@Service
public class FileHashService {
    // SHA-256 hash generation
    // Integrity verification
    // Unique file identification
}
```

**🎲 IVGenerator**
```java
@Component
public class IVGenerator {
    // Secure random IV generation
    // GCM nonce management
}
```

**📦 KeyEnvelopeBuilder**
```java
@Service
public class KeyEnvelopeBuilder {
    // Multi-user key envelope creation
    // Encrypted key packaging
    // Recipient management
}
```

**🎼 CryptoOrchestrator**
```java
@Service
public class CryptoOrchestrator {
    // End-to-end encryption workflow
    // Key management coordination
    // Multi-recipient handling
}
```

#### Project Structure:
```
encryption-service/
├── src/main/java/com/omvaultchain/encryption/
│   ├── controller/CryptoController.java
│   ├── service/
│   │   ├── AESService.java
│   │   ├── AsymmetricEncryptionService.java
│   │   ├── FileHashService.java
│   │   ├── IVGenerator.java
│   │   ├── KeyEnvelopeBuilder.java
│   │   └── CryptoOrchestrator.java
│   ├── model/
│   │   ├── EncryptionRequest.java
│   │   ├── EncryptionResponse.java
│   │   └── KeyEnvelope.java
│   └── config/CryptoConfig.java
├── Dockerfile
└── pom.xml
```

---

### 📦 storage-service
**Technology:** Spring Boot + IPFS Client + Pinata API

#### Internal Components:

**🌐 IPFSClient**
```java
@Service
public class IPFSClient {
    // Pinata/Web3.Storage integration
    // File upload/download
    // Pin management
    // Gateway communication
}
```

**✅ CIDVerifier**
```java
@Service
public class CIDVerifier {
    // CID validation
    // Hash verification
    // Content integrity checks
}
```

**📊 MetadataExtractor**
```java
@Service
public class MetadataExtractor {
    // File metadata extraction
    // MIME type detection
    // Size calculation
    // Upload timestamp
}
```

**📦 BatchUploader**
```java
@Service
public class BatchUploader {
    // Multi-file upload
    // Merkle root generation
    // Batch processing
}
```

#### Project Structure:
```
storage-service/
├── src/main/java/com/omvaultchain/storage/
│   ├── controller/FileStorageController.java
│   ├── service/
│   │   ├── IPFSClient.java
│   │   ├── CIDVerifier.java
│   │   ├── MetadataExtractor.java
│   │   └── BatchUploader.java
│   ├── model/
│   │   ├── FileMetadata.java
│   │   ├── UploadRequest.java
│   │   └── UploadResponse.java
│   └── config/StorageConfig.java
├── Dockerfile
└── pom.xml
```

---

### ⛓️ blockchain-service
**Technology:** Spring Boot + web3j + Solidity

#### Internal Components:

**🔗 SmartContractClient**
```java
@Service
public class SmartContractClient {
    // web3j integration
    // Contract deployment
    // Method calling
    // Event listening
}
```

**📋 FileRegistryService**
```java
@Service
public class FileRegistryService {
    // File registration on-chain
    // CID storage
    // Hash anchoring
    // Metadata linking
}
```

**🔑 AccessRightsService**
```java
@Service
public class AccessRightsService {
    // Access grant/revoke
    // Permission management
    // Rights validation
    // User access tracking
}
```

**🔄 VersioningService**
```java
@Service
public class VersioningService {
    // Version registration
    // Version linking
    // History maintenance
    // Rollback support
}
```

**🗂️ BlockchainMetadataMapper**
```java
@Component
public class BlockchainMetadataMapper {
    // Solidity data parsing
    // Type conversion
    // Structure mapping
}
```

**👂 EventListenerService**
```java
@Service
public class EventListenerService {
    // Smart contract event listening
    // Event processing
    // Notification dispatching
}
```

#### Project Structure:
```
blockchain-service/
├── src/main/java/com/omvaultchain/blockchain/
│   ├── controller/BlockchainController.java
│   ├── service/
│   │   ├── SmartContractClient.java
│   │   ├── FileRegistryService.java
│   │   ├── AccessRightsService.java
│   │   ├── VersioningService.java
│   │   ├── BlockchainMetadataMapper.java
│   │   └── EventListenerService.java
│   ├── model/
│   │   ├── FileRecord.java
│   │   ├── AccessRecord.java
│   │   └── VersionRecord.java
│   └── config/BlockchainConfig.java
├── contracts/
│   ├── FileRegistry.sol
│   ├── AccessControl.sol
│   └── VersionManager.sol
├── Dockerfile
└── pom.xml
```

---

### 🔐 access-control-service
**Technology:** Spring Boot + Spring Security

#### Internal Components:

**✅ AccessGrantService**
```java
@Service
public class AccessGrantService {
    // Access sharing workflow
    // Key encryption for recipients
    // Blockchain registration
    // Notification handling
}
```

**❌ AccessRevokeService**
```java
@Service
public class AccessRevokeService {
    // Access revocation
    // Blockchain update
    // Soft/hard revocation
    // Audit logging
}
```

**🔍 AccessValidator**
```java
@Service
public class AccessValidator {
    // Permission validation
    // On-chain verification
    // Access status checking
    // Rights enforcement
}
```

**🚚 AESKeyDeliveryService**
```java
@Service
public class AESKeyDeliveryService {
    // Encrypted key delivery
    // Version-specific keys
    // Recipient validation
    // Key rotation
}
```

**👥 GroupAccessService**
```java
@Service
public class GroupAccessService {
    // Group permission management
    // Bulk access operations
    // Role-based access
    // Enterprise features
}
```

#### Project Structure:
```
access-control-service/
├── src/main/java/com/omvaultchain/access/
│   ├── controller/AccessController.java
│   ├── service/
│   │   ├── AccessGrantService.java
│   │   ├── AccessRevokeService.java
│   │   ├── AccessValidator.java
│   │   ├── AESKeyDeliveryService.java
│   │   └── GroupAccessService.java
│   ├── model/
│   │   ├── AccessGrant.java
│   │   ├── AccessRevoke.java
│   │   └── AccessStatus.java
│   └── config/AccessConfig.java
├── Dockerfile
└── pom.xml
```

---

### 📊 metadata-service
**Technology:** Spring Boot + MySQL + JPA

#### Internal Components:

**📋 FileMetadataRegistry**
```java
@Service
public class FileMetadataRegistry {
    // Metadata registration
    // CID mapping
    // Owner tracking
    // File information storage
}
```

**🔄 VersionManager**
```java
@Service
public class VersionManager {
    // Version linking
    // History maintenance
    // Version comparison
    // Rollback support
}
```

**🔍 MetadataQueryService**
```java
@Service
public class MetadataQueryService {
    // Metadata retrieval
    // Search functionality
    // Filtering options
    // Pagination support
}
```

**🔄 MetadataMapper**
```java
@Component
public class MetadataMapper {
    // Data transformation
    // Frontend formatting
    // Size conversion
    // Date formatting
}
```

**👤 FileOwnerValidator**
```java
@Service
public class FileOwnerValidator {
    // Ownership verification
    // Modification rights
    // Access control
    // Security validation
}
```

#### Project Structure:
```
metadata-service/
├── src/main/java/com/omvaultchain/metadata/
│   ├── controller/MetadataController.java
│   ├── service/
│   │   ├── FileMetadataRegistry.java
│   │   ├── VersionManager.java
│   │   ├── MetadataQueryService.java
│   │   ├── MetadataMapper.java
│   │   └── FileOwnerValidator.java
│   ├── model/
│   │   ├── FileMetadata.java
│   │   ├── VersionInfo.java
│   │   └── FileStats.java
│   ├── repository/
│   │   ├── FileMetadataRepository.java
│   │   └── VersionRepository.java
│   └── config/MetadataConfig.java
├── Dockerfile
└── pom.xml
```

---

## 📊 Database Schema

### MySQL Tables

**users**
```sql
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,
    wallet_address VARCHAR(42) UNIQUE NOT NULL,
    public_key TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_wallet_address (wallet_address),
    INDEX idx_created_at (created_at)
);
```

**files**
```sql
CREATE TABLE files (
    id CHAR(36) PRIMARY KEY,
    owner_id CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(100),
    size_bytes BIGINT,
    cid VARCHAR(100) UNIQUE NOT NULL,
    file_hash VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_owner_id (owner_id),
    INDEX idx_cid (cid),
    INDEX idx_file_hash (file_hash),
    INDEX idx_created_at (created_at)
);
```

**file_versions**
```sql
CREATE TABLE file_versions (
    id CHAR(36) PRIMARY KEY,
    file_id CHAR(36) NOT NULL,
    version_number INTEGER NOT NULL,
    cid VARCHAR(100) UNIQUE NOT NULL,
    file_hash VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_current BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE,
    INDEX idx_file_id (file_id),
    INDEX idx_version_number (version_number),
    INDEX idx_cid (cid),
    INDEX idx_is_current (is_current)
);
```

**access_permissions**
```sql
CREATE TABLE access_permissions (
    id CHAR(36) PRIMARY KEY,
    file_id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,
    encrypted_key TEXT NOT NULL,
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    revoked_at TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_file_user (file_id, user_id),
    INDEX idx_file_id (file_id),
    INDEX idx_user_id (user_id),
    INDEX idx_is_active (is_active),
    INDEX idx_granted_at (granted_at)
);
```

**audit_logs**
```sql
CREATE TABLE audit_logs (
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36),
    action VARCHAR(50) NOT NULL,
    resource_type VARCHAR(50) NOT NULL,
    resource_id CHAR(36),
    details JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_resource_type (resource_type),
    INDEX idx_created_at (created_at)
);
```

### Redis Cache Structure

**Session Cache**
```
session:{wallet_address} -> {jwt_token, expires_at}
```

**Challenge Cache**
```
challenge:{wallet_address} -> {nonce, expires_at}
```

**File Cache**
```
file:{file_id} -> {metadata, access_list, version_info}
```

**User Cache**
```
user:{wallet_address} -> {user_profile, public_key}
```

---

## 🚀 Development Guidelines

### Project Structure

```
om-vaultchain/
├── services/
│   ├── auth-service/
│   ├── encryption-service/
│   ├── storage-service/
│   ├── blockchain-service/
│   ├── access-control-service/
│   └── metadata-service/
├── gateway/
│   └── api-gateway/
├── frontend/
│   ├── user-app/
│   └── admin-app/
├── contracts/
│   ├── FileRegistry.sol
│   ├── AccessControl.sol
│   └── VersionManager.sol
├── docker-compose.yml
├── .env.example
└── README.md
```

### Development Standards

**Code Quality**
- Unit test coverage >80%
- Integration tests for all APIs
- Security audit for all endpoints
- Performance benchmarks

**Security Requirements**
- All file operations must use client-side encryption
- Wallet signature verification required
- Rate limiting on all endpoints
- Comprehensive audit logging

**Performance Targets**
- File upload: <5s for 10MB files
- File access: <2s response time
- System availability: 99.9%
- Concurrent users: 10,000+

### Technology Versions

- **Java**: 17+
- **Spring Boot**: 3.2+
- **MySQL**: 8.0+
- **Redis**: 7.0+
- **Node.js**: 18+
- **React**: 18+
- **Solidity**: 0.8.19+

### Deployment Architecture

```
Production Environment:
├── Load Balancer (nginx)
├── API Gateway (2 instances)
├── Microservices (3 instances each)
├── Database Cluster (MySQL Master/Slave)
├── Cache Cluster (Redis Sentinel)
└── Monitoring (Prometheus + Grafana)
```

---

## 🔐 Security Considerations

### Encryption Standards
- **AES-256-GCM** for file encryption
- **RSA-4096/ECIES** for key encryption
- **SHA-256** for file hashing
- **EIP-191** for wallet signatures

### Access Control
- Zero-knowledge architecture
- Blockchain-anchored permissions
- Revocable access rights
- Comprehensive audit trails

### Data Protection
- No plaintext file storage
- Encrypted keys per user
- Secure key rotation
- GDPR compliance

### Smart Contract Security
- Multi-signature wallet integration
- Upgradeable proxy pattern
- Emergency pause functionality
- Gas optimization strategies

---

## 📱 Mobile Support

### Flutter Mobile App
- Cross-platform iOS/Android support
- Native wallet integration
- Biometric authentication
- Offline file management
- Push notifications for access grants

### Progressive Web App (PWA)
- Mobile-optimized interface
- Offline functionality
- Push notifications
- App-like experience

---

## 🌍 Scalability & Performance

### Horizontal Scaling
- Kubernetes deployment
- Auto-scaling based on load
- Database sharding strategies
- CDN for static assets

### Optimization Strategies
- Database query optimization
- Redis caching layers
- IPFS gateway load balancing
- Blockchain call batching

---

**Built with ❤️ by the OM VaultChain Team**
