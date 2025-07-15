const { ethers } = require("hardhat");

async function main() {
    const [deployer] = await ethers.getSigners();
    console.log("Deploying contracts with the account:", deployer.address);

    // Deploy FileRegistry
    const FileRegistry = await ethers.getContractFactory("FileRegistry");
    const fileRegistry = await FileRegistry.deploy();
    const fileRegistryAddress = await fileRegistry.getAddress();
    console.log("✅ FileRegistry deployed to:", fileRegistryAddress);

    // Deploy AccessControl
    const AccessControl = await ethers.getContractFactory("AccessControl");
    const accessControl = await AccessControl.deploy();
    const accessControlAddress = await accessControl.getAddress();
    console.log("✅ AccessControl deployed to:", accessControlAddress);

    // Deploy VersionManager
    const VersionManager = await ethers.getContractFactory("VersionManager");
    const versionManager = await VersionManager.deploy();
    const versionManagerAddress = await versionManager.getAddress();
    console.log("✅ VersionManager deployed to:", versionManagerAddress);

    // Optional: Save addresses to file for backend/frontend use
    const fs = require('fs');
    const deployed = {
        FileRegistry: fileRegistryAddress,
        AccessControl: accessControlAddress,
        VersionManager: versionManagerAddress
    };
    fs.writeFileSync('./deployed_contracts.json', JSON.stringify(deployed, null, 2));
}

main().catch((error) => {
    console.error(error);
    process.exitCode = 1;
});