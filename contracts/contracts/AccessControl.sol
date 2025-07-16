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

    // Mapping to track which addresses were granted access per CID (needed for listing and bulk revoke)
    mapping(string => address[]) private grantedUsers;

    event AccessGranted(string cid, address user, uint256 timestamp, string encryptedKey);
    event AccessRevoked(string cid, address user, uint256 timestamp);
    event AccessBulkGranted(string cid, uint count, uint256 timestamp);
    event AccessAllRevoked(string cid, uint256 timestamp);

    /// Grant access and store encrypted AES key
    function grantAccess(string memory cid, address user, string memory encryptedKey) public {
        if (!access[cid][user].granted) {
            grantedUsers[cid].push(user);
        }
        access[cid][user] = Permission(true, block.timestamp, encryptedKey);
        emit AccessGranted(cid, user, block.timestamp, encryptedKey);
    }

    /// Grant access to multiple users at once
    function grantMultipleAccess(string memory cid, address[] memory users, string[] memory encryptedKeys) public {
        require(users.length == encryptedKeys.length, "Mismatched input lengths");
        for (uint256 i = 0; i < users.length; i++) {
            grantAccess(cid, users[i], encryptedKeys[i]);
        }
        emit AccessBulkGranted(cid, users.length, block.timestamp);
    }

    /// Revoke access from a single user
    function revokeAccess(string memory cid, address user) public {
        access[cid][user].granted = false;
        access[cid][user].grantedAt = block.timestamp;
        emit AccessRevoked(cid, user, block.timestamp);
    }

    /// Revoke access from all users of a file
    function revokeAllAccess(string memory cid) public {
        address[] memory users = grantedUsers[cid];
        for (uint256 i = 0; i < users.length; i++) {
            access[cid][users[i]].granted = false;
            access[cid][users[i]].grantedAt = block.timestamp;
            emit AccessRevoked(cid, users[i], block.timestamp);
        }
        emit AccessAllRevoked(cid, block.timestamp);
    }

    /// Check if a user has access to a file (CID)
    function hasAccess(string memory cid, address user) public view returns (bool) {
        return access[cid][user].granted;
    }

    /// View full access record (granted status, timestamp, encrypted key)
    function getAccess(string memory cid, address user)
    public
    view
    returns (bool, uint256, string memory)
    {
        Permission memory p = access[cid][user];
        return (p.granted, p.grantedAt, p.encryptedKey);
    }

    /// Return a list of currently active users for a given CID
    function getAccessList(string memory cid) public view returns (address[] memory) {
        address[] memory allUsers = grantedUsers[cid];
        uint256 count = 0;

        // Count active users
        for (uint256 i = 0; i < allUsers.length; i++) {
            if (access[cid][allUsers[i]].granted) {
                count++;
            }
        }

        address[] memory activeUsers = new address[](count);
        uint256 j = 0;
        for (uint256 i = 0; i < allUsers.length; i++) {
            if (access[cid][allUsers[i]].granted) {
                activeUsers[j++] = allUsers[i];
            }
        }

        return activeUsers;
    }
}
