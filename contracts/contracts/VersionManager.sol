// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.10;

contract VersionManager {
    struct Version {
        string cid;
        uint256 version;
        uint256 timestamp;
    }

    enum FileStatus { None, Current, Deleted, RolledBack }

    // Mappings
    mapping(string => Version[]) private versions;
    mapping(string => uint256) private currentVersionIndex;
    mapping(string => FileStatus) private fileStatus;
    mapping(string => mapping(uint256 => bool)) private lockedVersions;

    // Events
    event VersionAdded(string fileId, string cid, uint256 version, uint256 timestamp);
    event FileStatusChanged(string indexed fileId, FileStatus status);
    event RolledBack(string indexed fileId, uint256 toVersion);
    event VersionLocked(string indexed fileId, uint256 version);

    // Add a new version for a file
    function addVersion(string memory fileId, string memory cid) public {
        uint256 newVersion = versions[fileId].length + 1;
        versions[fileId].push(Version(cid, newVersion, block.timestamp));
        currentVersionIndex[fileId] = newVersion;
        fileStatus[fileId] = FileStatus.Current;

        emit VersionAdded(fileId, cid, newVersion, block.timestamp);
        emit FileStatusChanged(fileId, FileStatus.Current);
    }

    // Get total number of versions
    function getVersionCount(string memory fileId) public view returns (uint256) {
        return versions[fileId].length;
    }

    // Get version at a specific index
    function getVersionAt(string memory fileId, uint256 index)
    public
    view
    returns (string memory cid, uint256 version, uint256 timestamp)
    {
        require(index < versions[fileId].length, "Index out of bounds");
        Version storage v = versions[fileId][index];
        return (v.cid, v.version, v.timestamp);
    }

    // Get the current version details
    function getCurrentVersion(string memory fileId)
    public
    view
    returns (string memory cid, uint256 version, uint256 timestamp)
    {
        uint256 idx = currentVersionIndex[fileId];
        require(idx > 0 && idx <= versions[fileId].length, "No current version");
        Version storage v = versions[fileId][idx - 1];
        return (v.cid, v.version, v.timestamp);
    }

    // Get the current status of the file
    function getFileStatus(string memory fileId) public view returns (string memory) {
        FileStatus status = fileStatus[fileId];
        if (status == FileStatus.Current) return "current";
        if (status == FileStatus.Deleted) return "deleted";
        if (status == FileStatus.RolledBack) return "rolled_back";
        return "none";
    }

    // Delete the file logically
    function deleteFile(string memory fileId) public {
        require(fileStatus[fileId] != FileStatus.Deleted, "File already deleted");
        fileStatus[fileId] = FileStatus.Deleted;
        currentVersionIndex[fileId] = 0;
        emit FileStatusChanged(fileId, FileStatus.Deleted);
    }

    // Roll back to a previous version
    function rollbackToVersion(string memory fileId, uint256 versionNumber) public {
        require(versionNumber > 0 && versionNumber <= versions[fileId].length, "Invalid version");
        require(fileStatus[fileId] != FileStatus.Deleted, "File is deleted");
        require(!lockedVersions[fileId][versionNumber], "Version is locked");

        currentVersionIndex[fileId] = versionNumber;
        fileStatus[fileId] = FileStatus.RolledBack;
        emit RolledBack(fileId, versionNumber);
        emit FileStatusChanged(fileId, FileStatus.RolledBack);
    }

    // Lock a version to prevent rollback
    function lockVersion(string memory fileId, uint256 versionNumber) public {
        require(versionNumber > 0 && versionNumber <= versions[fileId].length, "Invalid version");
        lockedVersions[fileId][versionNumber] = true;
        emit VersionLocked(fileId, versionNumber);
    }

    // Compare two versions of a file
    function compareVersions(string memory fileId, uint256 version1, uint256 version2) public view returns (bool) {
        require(version1 > 0 && version1 <= versions[fileId].length, "Invalid version1");
        require(version2 > 0 && version2 <= versions[fileId].length, "Invalid version2");

        string memory cid1 = versions[fileId][version1 - 1].cid;
        string memory cid2 = versions[fileId][version2 - 1].cid;

        return (keccak256(abi.encodePacked(cid1)) == keccak256(abi.encodePacked(cid2)));
    }

    // Check if a version is locked
    function isVersionLocked(string memory fileId, uint256 versionNumber) public view returns (bool) {
        return lockedVersions[fileId][versionNumber];
    }
}
