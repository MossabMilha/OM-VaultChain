# OM VaultChain API Documentation

## 🚀 Overview

This is the Laravel backend API for the OM VaultChain project. The API provides authentication, file management, and blockchain integration capabilities.

## 📋 API Status

| Component | Status | Description |
|-----------|--------|-------------|
| ✅ API Routes | Complete | `/routes/api.php` configured |
| ✅ Authentication | Complete | Laravel Sanctum integration |
| ✅ CORS Middleware | Complete | Cross-origin requests enabled |
| ✅ Health Check | Complete | API health monitoring |
| ✅ User Management | Complete | Registration, login, logout |
| 🟡 File Management | Partial | Basic CRUD operations (needs IPFS integration) |
| 🕓 Blockchain Integration | Pending | Smart contract interaction |
| 🕓 Database Models | Pending | File and transaction models |

## 🛠️ Setup Instructions

### 1. Install Dependencies
```bash
cd backend
composer install
```

### 2. Environment Configuration
Ensure your `.env` file has the correct database settings:
```env
DB_CONNECTION=sqlite
# For MySQL, uncomment and configure:
# DB_HOST=127.0.0.1
# DB_PORT=3306
# DB_DATABASE=omvaultchain
# DB_USERNAME=your_username
# DB_PASSWORD=your_password
```

### 3. Run Migrations
```bash
php artisan migrate
```

### 4. Start Development Server
```bash
php artisan serve
```

## 📡 API Endpoints

### Base URL
```
http://localhost:8000/api/v1
```

### 🔍 Health Check
```http
GET /health
```
**Response:**
```json
{
  "status": "OK",
  "timestamp": "2025-07-23T12:59:14.024965Z",
  "service": "OM VaultChain API",
  "version": "1.0.0"
}
```

### 🔐 Authentication

#### Register User
```http
POST /register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "password_confirmation": "password123"
}
```

#### Login User
```http
POST /login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

#### Get Current User
```http
GET /me
Authorization: Bearer {token}
```

#### Logout
```http
POST /logout
Authorization: Bearer {token}
```

### 📁 File Management (Protected Routes)

#### List Files
```http
GET /files
Authorization: Bearer {token}
```

#### Upload File
```http
POST /files
Authorization: Bearer {token}
Content-Type: multipart/form-data

{
  "file": [file],
  "name": "optional-filename",
  "description": "optional description"
}
```

#### Get File Details
```http
GET /files/{id}
Authorization: Bearer {token}
```

#### Update File Metadata
```http
PUT /files/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "new-filename",
  "description": "updated description"
}
```

#### Delete File
```http
DELETE /files/{id}
Authorization: Bearer {token}
```

#### Download File
```http
GET /files/{id}/download
Authorization: Bearer {token}
```

## 🔧 Development Notes

### Missing Components to Implement:

1. **Database Models**
   - File model with IPFS hash, metadata
   - Transaction model for blockchain records
   - User file permissions

2. **IPFS Integration**
   - Connect to storage service
   - Upload/retrieve files from IPFS
   - Generate and store IPFS hashes

3. **Blockchain Integration**
   - Smart contract interaction
   - Transaction recording
   - File verification

4. **Advanced Features**
   - File versioning
   - Access control
   - File sharing
   - Encryption/decryption

### Next Steps:
1. Create database migrations for files and transactions
2. Implement IPFS service integration
3. Add blockchain service communication
4. Create comprehensive tests
5. Add rate limiting and security middleware

## 🧪 Testing

To test the API endpoints, you can use tools like:
- Postman
- Insomnia
- curl commands
- Laravel's built-in testing

Example test command:
```bash
php artisan test
```

## 🔒 Security Features

- ✅ Laravel Sanctum for API authentication
- ✅ CORS middleware for cross-origin requests
- ✅ Input validation on all endpoints
- ✅ Password hashing
- ✅ Token-based authentication

## 📝 Error Handling

All API responses follow a consistent format:
```json
{
  "success": true/false,
  "message": "Description of the result",
  "data": {}, // Present on success
  "errors": {} // Present on validation errors
}
```

HTTP Status Codes:
- `200` - Success
- `201` - Created
- `401` - Unauthorized
- `422` - Validation Error
- `500` - Server Error
