# 🔐 OM VaultChain - Complete Technical Architecture

> **SaaS Platform for Encrypted File Storage with Blockchain Access Control**

## 🏗️ Complete Technology Stack

| Layer | Component | Technology |
|-------|-----------|------------|
| 🔐 **Encryption** | AES-256-GCM, RSA/ECIES | Java + BouncyCastle |
| 📦 **Storage** | Decentralized Storage | IPFS with Pinata API |
| ⛓️ **Smart Contracts** | Blockchain Layer | Solidity on Polygon + Hardhat |
| 🌐 **Blockchain SDK** | Backend Integration | web3j (Java) |
| 🔧 **Backend Coordination** | Microservices | Spring Boot |
| 📊 **Metadata Format** | Data Structure | Custom JSON structure |
| 🔑 **Wallet/Auth** | Authentication | MetaMask, WalletConnect |
| 🖥️ **Frontend** | User Interface | React (web) or Flutter (mobile) |
| 🗄️ **Database** | Metadata Storage | PostgreSQL + MongoDB |
| 📡 **Message Queue** | Service Communication | Apache Kafka |
| 🔍 **Search Engine** | File Search | Elasticsearch |
| 📈 **Monitoring** | System Observability | Prometheus + Grafana |

---

## 🧩 Missing Microservices Architecture

### 📊 audit-log-service
**🎯 Role:** Track all system activities and maintain comprehensive audit trails

**Technologies:** Spring Boot + PostgreSQL + Elasticsearch

**🧱 Internal Components:**
- **AuditEventLogger**
  - Captures all file operations (upload, download, share, revoke)
  - Records blockchain transactions
  - Logs authentication events
- **ComplianceReporter**
  - Generates compliance reports (GDPR, SOX, etc.)
  - Automated reporting for legal requirements
- **SecurityAuditAnalyzer**
  - Detects suspicious activities
  - Anomaly detection using ML algorithms
- **EventSearchEngine**
  - Elasticsearch integration for fast log searching
  - Real-time event filtering and analysis

**📁 Project Structure:**
```
audit-log-service/
├── src/main/java/com/omvaultchain/audit/
│   ├── controller/
│   │   └── AuditController.java
│   ├── service/
│   │   ├── AuditEventLogger.java
│   │   ├── ComplianceReporter.java
│   │   └── SecurityAuditAnalyzer.java
│   ├── model/
│   │   ├── AuditEvent.java
│   │   └── ComplianceReport.java
│   └── repository/
│       └── AuditEventRepository.java
├── Dockerfile
└── pom.xml
```

---

### 🔔 notification-service
**🎯 Role:** Handle all platform notifications and alerts

**Technologies:** Spring Boot + Apache Kafka + Redis

**🧱 Internal Components:**
- **NotificationDispatcher**
  - Email notifications (file shared, access revoked)
  - In-app notifications
  - Push notifications for mobile
- **AlertManager**
  - System alerts (storage limits, security issues)
  - Admin notifications for incidents
- **TemplateEngine**
  - Email template management
  - Multi-language support
- **NotificationPreferences**
  - User notification settings
  - Subscription management

**📁 Project Structure:**
```
notification-service/
├── src/main/java/com/omvaultchain/notification/
│   ├── controller/
│   │   └── NotificationController.java
│   ├── service/
│   │   ├── NotificationDispatcher.java
│   │   ├── AlertManager.java
│   │   └── TemplateEngine.java
│   ├── model/
│   │   ├── Notification.java
│   │   └── NotificationTemplate.java
│   └── config/
│       └── KafkaConfig.java
├── Dockerfile
└── pom.xml
```

---

### 💰 billing-service
**🎯 Role:** Manage subscriptions, payments, and usage tracking

**Technologies:** Spring Boot + Stripe API + PostgreSQL

**🧱 Internal Components:**
- **SubscriptionManager**
  - Plan management (Free, Premium, Enterprise)
  - Subscription lifecycle
  - Usage limits enforcement
- **PaymentProcessor**
  - Stripe integration for card payments
  - Web3 payment processing (crypto)
  - Invoice generation
- **UsageTracker**
  - Storage consumption monitoring
  - API call tracking
  - Bandwidth usage calculation
- **BillingCalculator**
  - Cost calculation based on usage
  - Proration for plan changes
  - Tax calculation by region

**📁 Project Structure:**
```
billing-service/
├── src/main/java/com/omvaultchain/billing/
│   ├── controller/
│   │   └── BillingController.java
│   ├── service/
│   │   ├── SubscriptionManager.java
│   │   ├── PaymentProcessor.java
│   │   └── UsageTracker.java
│   ├── model/
│   │   ├── Subscription.java
│   │   ├── Payment.java
│   │   └── UsageMetrics.java
│   └── integration/
│       ├── StripeIntegration.java
│       └── Web3PaymentGateway.java
├── Dockerfile
└── pom.xml
```

---

### 🔍 search-service
**🎯 Role:** Provide advanced search capabilities across encrypted files

**Technologies:** Spring Boot + Elasticsearch + Apache Lucene

**🧱 Internal Components:**
- **FileIndexer**
  - Indexes file metadata (not content for security)
  - Search by name, type, date, owner
  - Tag-based searching
- **SearchQueryProcessor**
  - Query optimization
  - Fuzzy search implementation
  - Filter combinations
- **SearchResultRanker**
  - Relevance scoring
  - Personalized search results
  - Access-based filtering
- **SearchAnalytics**
  - Search pattern analysis
  - Popular files tracking
  - User behavior insights

**📁 Project Structure:**
```
search-service/
├── src/main/java/com/omvaultchain/search/
│   ├── controller/
│   │   └── SearchController.java
│   ├── service/
│   │   ├── FileIndexer.java
│   │   ├── SearchQueryProcessor.java
│   │   └── SearchResultRanker.java
│   ├── model/
│   │   ├── SearchQuery.java
│   │   └── SearchResult.java
│   └── config/
│       └── ElasticsearchConfig.java
├── Dockerfile
└── pom.xml
```

---

### 🛡️ security-service
**🎯 Role:** Handle security monitoring and threat detection

**Technologies:** Spring Boot + Apache Kafka + Redis + TensorFlow

**🧱 Internal Components:**
- **ThreatDetectionEngine**
  - Anomaly detection using ML
  - Suspicious activity monitoring
  - Brute force attack detection
- **SecurityPolicyEnforcer**
  - Access policy validation
  - Rate limiting enforcement
  - IP whitelisting/blacklisting
- **IncidentResponseManager**
  - Automated incident response
  - Security alert escalation
  - Forensic data collection
- **VulnerabilityScanner**
  - Regular security scans
  - Dependency vulnerability checks
  - Smart contract security analysis

**📁 Project Structure:**
```
security-service/
├── src/main/java/com/omvaultchain/security/
│   ├── controller/
│   │   └── SecurityController.java
│   ├── service/
│   │   ├── ThreatDetectionEngine.java
│   │   ├── SecurityPolicyEnforcer.java
│   │   └── IncidentResponseManager.java
│   ├── model/
│   │   ├── SecurityIncident.java
│   │   └── ThreatAnalysis.java
│   └── ml/
│       └── AnomalyDetector.java
├── Dockerfile
└── pom.xml
```

---

### 📈 analytics-service
**🎯 Role:** Provide business intelligence and usage analytics

**Technologies:** Spring Boot + Apache Spark + InfluxDB + Grafana

**🧱 Internal Components:**
- **UsageAnalyzer**
  - Platform usage statistics
  - User behavior analysis
  - Storage consumption trends
- **ReportGenerator**
  - Custom dashboard creation
  - Scheduled reports
  - Data visualization
- **MetricsCollector**
  - Real-time metrics collection
  - Performance monitoring
  - System health indicators
- **PredictiveAnalytics**
  - Usage forecasting
  - Capacity planning
  - Churn prediction

**📁 Project Structure:**
```
analytics-service/
├── src/main/java/com/omvaultchain/analytics/
│   ├── controller/
│   │   └── AnalyticsController.java
│   ├── service/
│   │   ├── UsageAnalyzer.java
│   │   ├── ReportGenerator.java
│   │   └── MetricsCollector.java
│   ├── model/
│   │   ├── UsageMetrics.java
│   │   └── AnalyticsReport.java
│   └── config/
│       └── SparkConfig.java
├── Dockerfile
└── pom.xml
```

---

## 🗄️ Database Architecture

### 📊 PostgreSQL Schema (Primary Database)
**Used by:** auth-service, metadata-service, billing-service, audit-log-service

**Key Tables:**
```sql
-- Users and Authentication
CREATE TABLE users (
    id UUID PRIMARY KEY,
    wallet_address VARCHAR(42) UNIQUE NOT NULL,
    public_key TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- File Metadata
CREATE TABLE file_metadata (
    id UUID PRIMARY KEY,
    file_id VARCHAR(64) UNIQUE NOT NULL,
    cid VARCHAR(64) NOT NULL,
    name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(100),
    size_bytes BIGINT,
    owner_address VARCHAR(42) NOT NULL,
    version INTEGER DEFAULT 1,
    previous_version_id UUID,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_address) REFERENCES users(wallet_address)
);

-- Access Control
CREATE TABLE file_access (
    id UUID PRIMARY KEY,
    file_id VARCHAR(64) NOT NULL,
    user_address VARCHAR(42) NOT NULL,
    granted_by VARCHAR(42) NOT NULL,
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    revoked_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (file_id) REFERENCES file_metadata(file_id),
    FOREIGN KEY (user_address) REFERENCES users(wallet_address)
);

-- Subscriptions
CREATE TABLE subscriptions (
    id UUID PRIMARY KEY,
    user_address VARCHAR(42) NOT NULL,
    plan_type VARCHAR(20) NOT NULL, -- FREE, PREMIUM, ENTERPRISE
    status VARCHAR(20) DEFAULT 'ACTIVE',
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ends_at TIMESTAMP,
    stripe_subscription_id VARCHAR(100),
    FOREIGN KEY (user_address) REFERENCES users(wallet_address)
);

-- Audit Logs
CREATE TABLE audit_events (
    id UUID PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    user_address VARCHAR(42),
    file_id VARCHAR(64),
    details JSONB,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 🍃 MongoDB Schema (Document Storage)
**Used by:** metadata-service, search-service, analytics-service

**Collections:**
```javascript
// File Documents with Rich Metadata
{
  "_id": ObjectId("..."),
  "fileId": "dcf9-22a1-45a4",
  "cid": "Qmbvdwxc...",
  "metadata": {
    "name": "contract_partner.pdf",
    "type": "application/pdf",
    "size": 2184000,
    "tags": ["contract", "legal", "partnership"],
    "description": "Contract signed by both parties"
  },
  "versions": [
    {
      "version": 1,
      "cid": "Qmabc123...",
      "uploadedAt": "2025-07-01T10:00:00Z",
      "changes": "Initial version"
    },
    {
      "version": 2,
      "cid": "Qmdef456...",
      "uploadedAt": "2025-07-02T14:30:00Z",
      "changes": "Added signatures"
    }
  ],
  "owner": "0xAbc123...",
  "sharedWith": ["0xDef456...", "0xGhi789..."],
  "createdAt": "2025-07-01T10:00:00Z",
  "updatedAt": "2025-07-02T14:30:00Z"
}

// Search Index
{
  "_id": ObjectId("..."),
  "fileId": "dcf9-22a1-45a4",
  "searchableText": "contract partner legal agreement",
  "tags": ["contract", "legal", "partnership"],
  "owner": "0xAbc123...",
  "accessibleBy": ["0xAbc123...", "0xDef456..."],
  "lastAccessed": "2025-07-03T09:15:00Z",
  "accessCount": 15
}
```

---

## 🔄 Message Queue Architecture (Apache Kafka)

### 📡 Kafka Topics
**Technology:** Apache Kafka with Zookeeper

**Topic Structure:**
```yaml
# File Operations
file-events:
  - file.uploaded
  - file.downloaded
  - file.shared
  - file.revoked
  - file.deleted

# User Events
user-events:
  - user.registered
  - user.login
  - user.logout
  - user.profile.updated

# Security Events
security-events:
  - security.threat.detected
  - security.access.denied
  - security.breach.attempted

# Billing Events
billing-events:
  - subscription.created
  - subscription.updated
  - payment.processed
  - usage.exceeded

# System Events
system-events:
  - service.started
  - service.stopped
  - health.check
```

---

## 🔒 Smart Contract Architecture

### 📜 Solidity Smart Contracts
**Technology:** Solidity + Hardhat + OpenZeppelin

**Contract Structure:**
```solidity
// VaultChainRegistry.sol
pragma solidity ^0.8.19;

import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";

contract VaultChainRegistry is Ownable, ReentrancyGuard {
    struct FileRecord {
        string cid;
        bytes32 fileHash;
        address owner;
        uint256 uploadTime;
        uint256 version;
        string previousVersionCid;
        bool isActive;
    }
    
    struct AccessGrant {
        address user;
        bytes encryptedAESKey;
        uint256 grantedAt;
        uint256 revokedAt;
        bool isActive;
    }
    
    // Mappings
    mapping(string => FileRecord) public files;
    mapping(string => mapping(address => AccessGrant)) public fileAccess;
    mapping(address => string[]) public userFiles;
    mapping(address => bytes) public userPublicKeys;
    
    // Events
    event FileUploaded(string indexed fileId, string cid, address indexed owner);
    event AccessGranted(string indexed fileId, address indexed user, address indexed grantedBy);
    event AccessRevoked(string indexed fileId, address indexed user, address indexed revokedBy);
    event VersionAdded(string indexed fileId, string newCid, uint256 version);
    
    // Functions
    function uploadFile(string memory fileId, string memory cid, bytes32 fileHash) external;
    function grantAccess(string memory fileId, address user, bytes memory encryptedKey) external;
    function revokeAccess(string memory fileId, address user) external;
    function addVersion(string memory fileId, string memory newCid, bytes32 newHash) external;
    function registerPublicKey(bytes memory publicKey) external;
}
```

---

## 🌐 API Gateway Enhancement

### 🔧 Advanced API Gateway Features
**Technology:** Spring Cloud Gateway + Netflix Zuul

**Enhanced Components:**
- **RateLimiter**
  - Per-user rate limiting
  - API endpoint throttling
  - DDoS protection
- **RequestValidator**
  - Input sanitization
  - Schema validation
  - Security headers enforcement
- **ResponseCacher**
  - Redis-based caching
  - CDN integration
  - Cache invalidation strategies
- **LoadBalancer**
  - Service discovery
  - Health check integration
  - Circuit breaker pattern

---

## 🖥️ Frontend Architecture Enhancement

### 🎨 React Frontend Architecture
**Technology:** React + TypeScript + Tailwind CSS + Vite

**Additional Components:**
```
frontend/
├── src/
│   ├── components/
│   │   ├── auth/
│   │   │   ├── WalletConnector.tsx
│   │   │   └── AuthGuard.tsx
│   │   ├── files/
│   │   │   ├── FileUpload.tsx
│   │   │   ├── FileList.tsx
│   │   │   ├── FileViewer.tsx
│   │   │   └── ShareModal.tsx
│   │   ├── dashboard/
│   │   │   ├── Analytics.tsx
│   │   │   ├── UsageMetrics.tsx
│   │   │   └── ActivityFeed.tsx
│   │   └── admin/
│   │       ├── UserManagement.tsx
│   │       ├── SystemHealth.tsx
│   │       └── AuditLogs.tsx
│   ├── hooks/
│   │   ├── useWallet.ts
│   │   ├── useEncryption.ts
│   │   └── useFileOperations.ts
│   ├── services/
│   │   ├── api.ts
│   │   ├── blockchain.ts
│   │   ├── encryption.ts
│   │   └── ipfs.ts
│   ├── utils/
│   │   ├── crypto.ts
│   │   ├── validation.ts
│   │   └── formatters.ts
│   └── types/
│       ├── file.ts
│       ├── user.ts
│       └── blockchain.ts
```

---

## 🔍 Monitoring & Observability

### 📊 Monitoring Stack
**Technology:** Prometheus + Grafana + Jaeger + ELK Stack

**Metrics Collection:**
- **Application Metrics**
  - Request/response times
  - Error rates
  - Throughput
- **Business Metrics**
  - File upload/download counts
  - User activity
  - Storage usage
- **Infrastructure Metrics**
  - CPU, memory, disk usage
  - Network latency
  - Database performance

---

## 🚀 Deployment Architecture

### 🐳 Containerization
**Technology:** Docker + Kubernetes + Helm

**Deployment Structure:**
```yaml
# kubernetes/
├── namespaces/
│   ├── production.yaml
│   ├── staging.yaml
│   └── development.yaml
├── services/
│   ├── auth-service/
│   ├── encryption-service/
│   ├── storage-service/
│   ├── blockchain-service/
│   ├── access-control-service/
│   └── metadata-service/
├── ingress/
│   ├── nginx-ingress.yaml
│   └── ssl-certificates.yaml
├── persistence/
│   ├── postgresql-pv.yaml
│   ├── mongodb-pv.yaml
│   └── redis-pv.yaml
└── monitoring/
    ├── prometheus.yaml
    ├── grafana.yaml
    └── alertmanager.yaml
```

---

## 🔐 Security Enhancements

### 🛡️ Additional Security Measures
- **WAF (Web Application Firewall)**
  - SQL injection prevention
  - XSS protection
  - Rate limiting
- **Secrets Management**
  - Kubernetes secrets
  - HashiCorp Vault integration
  - Key rotation policies
- **Network Security**
  - Service mesh (Istio)
  - mTLS encryption
  - Network policies

---

This comprehensive architecture provides a complete technical foundation for the OM VaultChain platform, incorporating all missing components and specifying the exact technologies to be used for each layer.
