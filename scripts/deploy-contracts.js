const { ethers } = require("hardhat");

async function main() {
  console.log("Deploying contracts to Ganache...");

  // Get the deployer account
  const [deployer] = await ethers.getSigners();
  console.log("Deploying contracts with account:", deployer.address);
  console.log("Account balance:", (await deployer.getBalance()).toString());

  // Deploy FileRegistry contract
  const FileRegistry = await ethers.getContractFactory("FileRegistry");
  const fileRegistry = await FileRegistry.deploy();
  await fileRegistry.deployed();
  console.log("FileRegistry deployed to:", fileRegistry.address);

  // Deploy AccessControl contract (if it exists)
  try {
    const AccessControl = await ethers.getContractFactory("AccessControl");
    const accessControl = await AccessControl.deploy();
    await accessControl.deployed();
    console.log("AccessControl deployed to:", accessControl.address);
  } catch (error) {
    console.log("AccessControl contract not found, skipping...");
  }

  // Deploy VersionManager contract (if it exists)
  try {
    const VersionManager = await ethers.getContractFactory("VersionManager");
    const versionManager = await VersionManager.deploy();
    await versionManager.deployed();
    console.log("VersionManager deployed to:", versionManager.address);
  } catch (error) {
    console.log("VersionManager contract not found, skipping...");
  }

  console.log("\nDeployment completed!");
  console.log("Update your .env file with these addresses:");
  console.log(`CONTRACT_FILE_REGISTRY=${fileRegistry.address}`);
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
