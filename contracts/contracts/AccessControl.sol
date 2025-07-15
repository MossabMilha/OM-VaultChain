// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;

contract AccessControl {
    struct Permission {
        bool granted;
        uint256 grantedAt;
        string encryptedKey;
    }

    // Mapping from CID → address → permission
    mapping(string => mapping(address => Permission)) private access;

    event AccessGranted(string cid, address user, uint256 timestamp, string encryptedKey);
    event AccessRevoked(string cid, address user, uint256 timestamp);

    /// Grant access and store encrypted AES key
    function grantAccess(string memory cid, address user, string memory encryptedKey) public {
        access[cid][user] = Permission(true, block.timestamp, encryptedKey);
        emit AccessGranted(cid, user, block.timestamp, encryptedKey);
    }

    /// Revoke access (sets granted to false but keeps encryptedKey history)
    function revokeAccess(string memory cid, address user) public {
        access[cid][user].granted = false;
        access[cid][user].grantedAt = block.timestamp;
        emit AccessRevoked(cid, user, block.timestamp);
    }

    /// View access status (granted, timestamp, encryptedKey)
    function getAccess(string memory cid, address user) public view returns (bool, uint256, string memory) {
        Permission memory p = access[cid][user];
        return (p.granted, p.grantedAt, p.encryptedKey);
    }
}
