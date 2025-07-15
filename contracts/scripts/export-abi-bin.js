const fs = require('fs');
const path = require('path');

const contracts = ['FileRegistry', 'AccessControl', 'VersionManager'];

contracts.forEach(name => {
    const inputPath = path.join(__dirname, `../artifacts/contracts/${name}.sol/${name}.json`);
    const artifact = JSON.parse(fs.readFileSync(inputPath, 'utf8'));

    fs.writeFileSync(`abi-bin/${name}.abi`, JSON.stringify(artifact.abi, null, 2));
    fs.writeFileSync(`abi-bin/${name}.bin`, artifact.bytecode.object || artifact.bytecode); // ethers v6 compatibility
});
