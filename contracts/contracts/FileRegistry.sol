// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.10;

contract FileRegistry {
    struct File{
        string cid;
        string fileHash;
        address owner;
        uint256 timestamp;
    }

    mapping(string => File) private files;

    event FileRegistered(string cid, string fileHash, address owner,uint256 timestamp);

    function registerFile(string memory cid,string memory fileHash) public{
        require(bytes(cid).length > 0,"CID Required");
        require(bytes(fileHash).length > 0,"File Hash Required");

        files[cid] = File(cid,fileHash,msg.sender,block.timestamp);
        emit FileRegistered(cid,fileHash,msg.sender,block.timestamp);
    }

    function getFile(string memory cid) public view returns(string memory,string memory,address,uint256){
        File memory f = files[cid];
        return (f.cid, f.fileHash, f.owner,f.timestamp);
    }
}
