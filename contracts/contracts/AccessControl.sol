// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;

contract AccessControl {
    struct Permission {
        bool granted;
        uint256 grantedAt;
    }

    mapping(string => mapping(address => Permission)) private access;

    event AccessGranted(string cid, address user, uint256 timestamp);
    event AccessRevoked(string cid, address user, uint256 timestamp);

    function grantAccess(string memory cid, address user) public {
        access[cid][user] = Permission(true, block.timestamp);
        emit AccessGranted(cid, user, block.timestamp);
    }

    function revokeAccess(string memory cid, address user) public {
        access[cid][user] = Permission(false, block.timestamp);
        emit AccessRevoked(cid, user, block.timestamp);
    }

    function hasAccess(string memory cid, address user) public view returns (bool, uint256) {
        Permission memory p = access[cid][user];
        return (p.granted, p.grantedAt);
    }
}
