#!/bin/bash

echo "Setting up blockchain environment..."

# Wait for Ganache to be ready
echo "Waiting for Ganache to start..."
sleep 10

# Navigate to contracts directory
cd contracts

# Install dependencies if needed
if [ ! -d "node_modules" ]; then
    echo "Installing contract dependencies..."
    npm install
fi

# Deploy contracts to Ganache
echo "Deploying contracts to Ganache..."
npx hardhat run scripts/deploy.js --network ganache

echo "Blockchain setup completed!"
