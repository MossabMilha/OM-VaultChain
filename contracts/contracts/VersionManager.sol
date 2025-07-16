// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.10;

contract VersionManager {
    struct Version {
        string cid;
        uint256 version;
        uint256 timestamp;
    }

    enum FileStatus { None, Current, Deleted, RolledBack }

    // Make versions private to avoid unsupported auto getter
    mapping(string => Version[]) private versions;
    mapping(string => uint256) private currentVersionIndex;
    mapping(string => FileStatus) private fileStatus;

    event VersionAdded(string fileId, string cid, uint256 version, uint256 timestamp);
    event FileStatusChanged(string indexed fileId, FileStatus status);
    event RolledBack(string indexed fileId, uint256 toVersion);

    // Add new version
    function addVersion(string memory fileId, string memory cid) public {
        uint256 newVersion = versions[fileId].length + 1;
        versions[fileId].push(Version(cid, newVersion, block.timestamp));
        currentVersionIndex[fileId] = newVersion;
        fileStatus[fileId] = FileStatus.Current;
        emit VersionAdded(fileId, cid, newVersion, block.timestamp);
        emit FileStatusChanged(fileId, FileStatus.Current);
    }

    // Get number of versions for a fileId
    function getVersionCount(string memory fileId) public view returns (uint256) {
        return versions[fileId].length;
    }

    // Get version info fields separately (no struct)
    function getVersionAt(string memory fileId, uint256 index) public view returns (
        string memory cid, uint256 version, uint256 timestamp
    ) {
        require(index < versions[fileId].length, "Index Out Of Bounds");
        Version storage v = versions[fileId][index];
        return (v.cid, v.version, v.timestamp);
    }

    // Get current version info
    function getCurrentVersion(string memory fileId) public view returns (
        string memory cid, uint256 version, uint256 timestamp
    ) {
        uint256 idx = currentVersionIndex[fileId];
        require(idx > 0 && idx <= versions[fileId].length, "No Current Version");
        Version storage v = versions[fileId][idx - 1];
        return (v.cid, v.version, v.timestamp);
    }

    // Get current status string
    function getFileStatus(string memory fileId) public view returns (string memory) {
        FileStatus status = fileStatus[fileId];
        if (status == FileStatus.Current) return "current";
        if (status == FileStatus.Deleted) return "deleted";
        if (status == FileStatus.RolledBack) return "rolled_back";
        return "none";
    }

    // Mark file deleted
    function deleteFile(string memory fileId) public {
        require(fileStatus[fileId] != FileStatus.Deleted, "File Already Deleted");
        fileStatus[fileId] = FileStatus.Deleted;
        currentVersionIndex[fileId] = 0;
        emit FileStatusChanged(fileId, FileStatus.Deleted);
    }

    // Rollback to version
    function rollbackToVersion(string memory fileId, uint256 versionNumber) public {
        require(versionNumber > 0 && versionNumber <= versions[fileId].length, "Invalid Version");
        require(fileStatus[fileId] != FileStatus.Deleted, "File Is Deleted");

        currentVersionIndex[fileId] = versionNumber;
        fileStatus[fileId] = FileStatus.RolledBack;
        emit RolledBack(fileId, versionNumber);
        emit FileStatusChanged(fileId, FileStatus.RolledBack);
    }
}
