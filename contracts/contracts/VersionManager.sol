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
    function getVersionCount(string memory fileId) public view returns (uint256) {
        return versions[fileId].length;
    }

    function getVersionAt(string memory fileId, uint256 index) public view returns (string memory, uint256, uint256) {
        Version memory v = versions[fileId][index];
        return (v.cid, v.version, v.timestamp);
    }
}
