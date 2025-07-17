# Blockchain Integration Test Setup

This guide explains how to set up and run **real blockchain integration tests** that interact with Ganache.

## 🚀 Quick Start

### 1. Start Ganache
```bash
# Make sure Ganache is running on http://localhost:7545
# You can use Ganache GUI or CLI
ganache-cli --port 7545 --deterministic
```

### 2. Deploy Test Contracts
```bash
cd services/blockchain-service
node deploy-test-contracts.js
```

### 3. Run Integration Tests
```bash
./mvnw test -Dspring.profiles.active=test
```

## 📋 Detailed Setup

### Prerequisites
- ✅ Ganache running on `http://localhost:7545`
- ✅ Node.js installed (for contract deployment)
- ✅ Contract artifacts in `contracts/abi-bin/`

### Step-by-Step Setup

#### 1. Install Dependencies
```bash
# Install web3 for contract deployment
npm install web3
```

#### 2. Start Ganache
```bash
# Option A: Ganache CLI
ganache-cli --port 7545 --deterministic --accounts 10 --gasLimit 10000000

# Option B: Ganache GUI
# - Set RPC Server to HTTP://127.0.0.1:7545
# - Set Gas Limit to 10,000,000
```

#### 3. Deploy Contracts
```bash
cd services/blockchain-service
node deploy-test-contracts.js
```

**Expected Output:**
```
🚀 Starting contract deployment to Ganache...
📋 Connected to Ganache with 10 accounts
📦 Deploying AccessControl...
✅ AccessControl deployed at: 0x5FbDB2315678afecb367f032d93F642f64180aa3
📦 Deploying FileRegistry...
✅ FileRegistry deployed at: 0xe7f1725E7734CE288F8367e1Bb143E90bb3F0512
📦 Deploying VersionManager...
✅ VersionManager deployed at: 0x9fE46736679d2D9a65F0992F2272dE9f3c7fa6e0
✅ Contract deployment complete!
✅ Test properties updated with new contract addresses
```

#### 4. Run Tests
```bash
# Run all integration tests
./mvnw test -Dspring.profiles.active=test

# Run specific test
./mvnw test -Dtest=RegisterFileApiTest -Dspring.profiles.active=test
```

## 🧪 What the Tests Do

### RegisterFileApiTest
- ✅ **Real API Call**: POST to `/blockchain/register-file`
- ✅ **Real Blockchain**: Calls actual smart contract on Ganache
- ✅ **Real Transaction**: Gets real transaction hash
- ✅ **Verification**: Reads back from blockchain to verify

### Expected Test Flow
1. 🚀 Test starts with real Spring Boot app
2. 📡 Connects to Ganache blockchain
3. 📝 Sends HTTP request to register file
4. ⛓️ Controller calls service → service calls smart contract
5. 💰 Real transaction is mined on Ganache
6. ✅ Test verifies file exists on blockchain

## 🔧 Configuration

### Test Properties (`application-test.properties`)
```properties
# Points to local Ganache
spring.web3j.client-address=http://localhost:7545

# Uses Ganache default account
blockchain.wallet.private-key=0x4f3edf983ac636a65a842ce7c78d9aa706d3b113bce9c46f30d7d21715b23b1d

# Contract addresses (updated by deployment script)
contract.address.access-control=0x5FbDB2315678afecb367f032d93F642f64180aa3
contract.address.file-registry=0xe7f1725E7734CE288F8367e1Bb143E90bb3F0512
contract.address.version-manager=0x9fE46736679d2D9a65F0992F2272dE9f3c7fa6e0
```

## 🐛 Troubleshooting

### "Failed to connect to Ganache"
- ✅ Check Ganache is running on port 7545
- ✅ Check no firewall blocking localhost:7545

### "Smart contracts not deployed"
- ✅ Run `node deploy-test-contracts.js`
- ✅ Check contract addresses in test properties

### "Transaction failed"
- ✅ Check gas limit in Ganache (should be 10M+)
- ✅ Check account has enough ETH

### "Tests are slow"
- ✅ Normal - blockchain tests take 5-10 seconds each
- ✅ Real transactions need time to be mined

## 🎯 Benefits of Integration Tests

✅ **Real Blockchain Interaction** - Tests actual smart contracts
✅ **End-to-End Validation** - Tests complete flow
✅ **Gas & Transaction Testing** - Catches real-world issues
✅ **Smart Contract Bugs** - Finds contract-level problems
✅ **Network Issues** - Tests blockchain connectivity

## 🚀 Next Steps

1. **Run the test** to see real blockchain interaction
2. **Add more integration tests** for other endpoints
3. **Create CI/CD pipeline** with automated Ganache setup
4. **Add performance tests** for gas optimization
