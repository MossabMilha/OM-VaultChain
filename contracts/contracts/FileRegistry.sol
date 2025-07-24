// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.10;

contract FileRegistry {
    struct File{
        string cid;
        string fileHash;
        address owner;
        address uploader;
        uint256 timestamp;
        uint256 version;
    }

    mapping(string => File) private files;

    event FileRegistered(address uploader,string cid, string fileHash, address owner,uint256 timestamp,uint256 version);

    function registerFile(address uploader,string memory cid,string memory fileHash,uint256 version) public{
        require(bytes(cid).length > 0,"CID Required");
        require(bytes(fileHash).length > 0,"File Hash Required");
        require(version > 0, "Version must be > 0");

        files[cid] = File(cid, fileHash, msg.sender, uploader,block.timestamp, version);
        emit FileRegistered(uploader,cid,fileHash,msg.sender,block.timestamp, version);
    }

    function getFile(string memory cid) public view returns(address,string memory,string memory,address,uint256,uint256){
        File memory f = files[cid];
        return (f.uploader,f.cid, f.fileHash, f.owner,f.timestamp,f.version);
    }
}
