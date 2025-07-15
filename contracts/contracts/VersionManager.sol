// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.10;

contract VersionManager {
    struct Version{
        string cid;
        uint256 version;
        uint256 timestamp;
    }
    mapping(string => Version[]) public versions;

    event VersionAdded(string fileId,string cid,uint256 version,uint256 timestamp);

    function addVersion(string memory fileId,string memory cid) public{
        uint256 newVersion = versions[fileId].length + 1;
        versions[fileId].push(Version(cid, newVersion, block.timestamp));
        emit VersionAdded(fileId, cid, newVersion, block.timestamp);
    }
    function getVersionHistory(string memory fileId) public view returns(Version[] memory) {
        return versions[fileId];
    }
}
