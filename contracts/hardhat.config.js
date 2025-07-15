require("dotenv").config({ path: "../.env" });
require("@nomicfoundation/hardhat-toolbox");

/** @type import('hardhat/config').HardhatUserConfig */
module.exports = {
  solidity: "0.8.28",
  networks: {
    localhost: {
      url: "http://127.0.0.1:7545",
      accounts: [process.env.PRIVATE_KEY]
    },
    ganache: {
      url: "http://localhost:7545",
      accounts: {
        mnemonic: "abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon about",
        count: 10
      },
      chainId: 1337,
      gas: 10000000,
      gasPrice: 20000000000
    }
  }
};
