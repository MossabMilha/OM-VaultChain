<?php

namespace App\Http\Controllers;

use App\Models\AccessPermission;
use App\Models\File;
use App\Models\FileBlockchainTransaction;
use App\Models\FileVersion;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Str;

class FileOrchestrationController extends Controller
{
    private $storageBase;
    private $blockchainBase;

    public function __construct()
    {
        $this->storageBase = config('services.storage_service.base_url');
        $this->blockchainBase = config('services.blockchain_service.base_url');
    }
    public function uploadSingleFile(Request $request){


        $validated = $request->validate([
            "encryptedFile"=>"required|string",
            "uploaderWallet"=>"required|string",
            "metadata.name"=>"required|string",
            "metadata.size"=>"required|integer",
            "metadata.mimeType"=>"required|string",
            "metadata.hash"=>"required|string",
            "metadata.encryptionKey"=>"required|string",
            "metadata.ownerId"=>"required|uuid",
            "metadata.iv" => "required|string"
        ]);


        $payload = [
            "ownerId" => $validated["metadata"]["ownerId"],
            "fileData" => $validated["encryptedFile"],
            "fileName" => $validated["metadata"]["name"],
            "mimeType" => $validated["metadata"]["mimeType"],
            "iv" => $validated["metadata"]["iv"],
            "encryptedKey" => $validated["metadata"]["encryptionKey"],
            "fileHash" => $validated["metadata"]["hash"]
        ];

        // 🔁 Send to storage-service
        $storageResponse = Http::post("{$this->storageBase}/upload/single", $payload);
        if (!$storageResponse->successful()) {
            return response()->json(['success' => false, 'message' => 'Storage service failed', 'error' => $storageResponse->body()], 500);
        }

        $storageData = $storageResponse->json();

        $fileVersion = FileVersion::create([
            "id" => Str::uuid(),
            "file_id" => $storageData['fileId'],
            "version_number" => 1,
            "cid" => $storageData['cid'],
            "file_hash" => $validated["metadata"]["hash"],
            "is_current" => true
        ]);

        // 🏗️ Prepare blockchain data


        $blockchainResponse = Http::post("{$this->blockchainBase}/register-file", [
            "uploaderWallet" => "0x3598b9413498353D666cA08367c602982DCc4931",
            "cid" => $storageData['cid'],
            "fileHash" => $validated["metadata"]["hash"],
            "version" => 1
        ]);

        if (!$blockchainResponse->successful()) {
            return response()->json(['success' => false, 'message' => 'Blockchain Service Failed', 'error' => $blockchainResponse->body()], 500);
        }
        $blockchainData = $blockchainResponse->json();

        FileBlockchainTransaction::create([
            "id" => Str::uuid(),
            "file_version_id" => $fileVersion->id,
            "tx_hash" => $blockchainData["data"]['transactionHash'],
            "block_number" => $blockchainData["data"]['blockNumber'],
            "chain_id" => $blockchainData["data"]['chainId'],
            "tx_status" => $blockchainData["data"]['transactionStatus']
        ]);



        return response()->json([
            "success" => true,
            "message" => "File uploaded and registered successfully.",
            "data" => [
                "fileId" => $storageData['fileId'],
                "fileName" => $validated["metadata"]["name"],
                "mimeType" => $validated["metadata"]["mimeType"],
                "sizeBytes" => $validated["metadata"]["size"],
                "ipfsCid" => $storageData['cid'],
                "blockchainTxHash" => $blockchainData["data"]['transactionHash'],
                "version" => 1,
                "ownerId" => $validated["metadata"]["ownerId"],
                "timestamp" => now()->toIso8601String(),
                "fileHash" => $validated["metadata"]["hash"]
            ]
        ], 201);

    }
    public function uploadBatchFile(Request $request)
    {

        $validated = $request->validate([
            "ownerId" => "required|uuid",
            "files" => "required|array",
            "files.*.fileName" => "required|string",
            "files.*.mimeType" => "required|string",
            "files.*.sizeBytes" => "required|integer",
            "files.*.iv" => "required|string",
            "files.*.fileHash" => "required|string",
            "files.*.encryptedKey" => "required|string",
            "files.*.fileData" => "required|string"
        ]);

        $payloads =[
            "ownerId" => $validated["ownerId"],
            "files" => $validated["files"]
        ];
        $storageResponse = Http::post("{$this->storageBase}/upload/batch", $payloads);
        if (!$storageResponse->successful()) {
            return response()->json(['success' => false, 'message' => 'Storage service failed', 'error' => $storageResponse->body()], 500);
        }
        $storageData = $storageResponse->json();

        $fileVersions = [];
        $blockchainResponses = [];

        foreach ($storageData as $index => $file) {

            $cid = $file['cid'];
            $fileHash = $validated["files"][$index]["fileHash"];

            // Query the Laravel database to get the file record by CID
            $fileRecord = File::where('cid', $cid)->first();
            if (!$fileRecord) {
                return response()->json([
                    'success' => false,
                    'message' => 'File record not found for CID: ' . $cid
                ], 500);
            }

            $fileId = $fileRecord->id;

            // Create file version for each file
            $fileVersion = FileVersion::create([
                "id" => Str::uuid(),
                "file_id" => $fileId,
                "version_number" => 1,
                "cid" => $cid,
                "file_hash" => $fileHash,
                "is_current" => true
            ]);

            $fileVersions[] = $fileVersion;

            // Register each file with blockchain service
            $blockchainResponse = Http::post("{$this->blockchainBase}/register-file", [
                "uploaderWallet" => "0x3598b9413498353D666cA08367c602982DCc4931",
                "cid" => $cid,
                "fileHash" => $fileHash,
                "version" => 1
            ]);

            if (!$blockchainResponse->successful()) {
                return response()->json([
                    'success' => false,
                    'message' => 'Blockchain Service Failed for file: ' . $file['fileName'],
                    'error' => $blockchainResponse->body()
                ], 500);
            }

            $blockchainData = $blockchainResponse->json();
            $blockchainResponses[] = $blockchainData;

            // Create blockchain transaction record for each file
            FileBlockchainTransaction::create([
                "id" => Str::uuid(),
                "file_version_id" => $fileVersion->id,
                "tx_hash" => $blockchainData["data"]['transactionHash'],
                "block_number" => $blockchainData["data"]['blockNumber'],
                "chain_id" => $blockchainData["data"]['chainId'],
                "tx_status" => $blockchainData["data"]['transactionStatus']
            ]);
        }

        // Merge storage and blockchain data
        $finalData = [
            'storage' => $storageData,
            'blockchain' => $blockchainResponses
        ];

        return response()->json([
            "success" => true,
            "message" => "Files Uploaded Successfully",
            "data" => $finalData
        ]);



    }
    public function listFiles(Request $request)
    {
        $validated = $request->validate([
            "userId" => "required|uuid"
        ]);

        $userId = $validated['userId'];

        // 🔹 Get all files shared with the user by eager loading file relation
        $sharedFiles = AccessPermission::with('file')
            ->active()
            ->where('user_id', $userId)
            ->whereHas('file', fn($q) => $q->where('is_deleted', false))
            ->get()
            ->pluck('file');

        // 🔹 Get all files owned by the user
        $ownedFiles = File::where('owner_id', $userId)
            ->where('is_deleted', false)
            ->get();

        // 🔹 Merge both collections, remove duplicates by 'id', sort by created_at or granted_at
        $allFiles = $ownedFiles->merge($sharedFiles)
            ->unique('id')
            ->values();

        // 🔹 Prepare the final array with all needed info
        $filesArray = $allFiles->map(function ($file) use ($userId) {
            // Determine if file is owned or shared
            $accessType = $file->owner_id === $userId ? 'owned' : 'shared';

            $userEncryptedKey = null;
            $grantedAt = null;

            if ($accessType === 'shared') {
                // Find the access permission record for this user and file
                $access = $file->accessPermissions()
                    ->where('user_id', $userId)
                    ->active()
                    ->first();

                if ($access) {
                    $userEncryptedKey = $access->encrypted_key;
                    $grantedAt = $access->granted_at;
                }
            } else {
                // For owned files
                $userEncryptedKey = $file->owner_encrypted_key;
                $grantedAt = $file->created_at;
            }

            return [
                'id' => $file->id,
                'owner_id' => $file->owner_id,
                'name' => $file->name,
                'mime_type' => $file->mime_type,
                'size_bytes' => $file->size_bytes,
                'cid' => $file->cid,
                'file_hash' => $file->file_hash,
                'iv' => $file->iv,
                'owner_encrypted_key' => $file->owner_encrypted_key,
                'granted_at' => $grantedAt,
                'user_encrypted_key' => $userEncryptedKey,
                'accessType' => $accessType,
            ];
        })->sortByDesc('granted_at')->values();

        return response()->json(["files" => $filesArray], 200);
    }
    public function listOwnedFiles(Request $request){
        $validated = $request->validate([
            "ownerId" => "required|uuid"
        ]);
        $files = File::where('owner_id', $validated['ownerId'])->get();

        return response()->json(["files"=>$files], 200);
    }
    public function downloadSingleFile(Request $request){
        $validated = $request->validate([
            "fileId" => "required|uuid",
            "userId" => "required|uuid"
        ]);
        $file = File::where('id', $validated['fileId'])->where('is_deleted', 0)->first();
        if (!$file) {
            return response()->json(['success' => false, 'message' => 'File Not Found'], 404);
        }

        $hasAccess = false;
        $encryptedKey = null;

        if($file->ownerId === $validated['userId']){
            $hasAccess = true;
            $encryptedKey = $file->encryptedKey;
        }else{
            $permission = AccessPermission::where('file_id', $file->id)
                ->where('user_id', $validated['userId'])
                ->active()
                ->first();
            if ($permission) {
                $hasAccess = true;
                $encryptedKey = $permission->encrypted_key;
            }
        }
        if (!$hasAccess) {
            return response()->json(['success' => false, 'message' => 'Access Denied'], 403);
        }
        $owner = User::find($file->owner_id);
        if (!$owner) {
            return response()->json(['success' => false, 'message' => 'Owner Not Found'], 500);
        }

        $payload = ["fileId" => $file->id];

        $StorageResponse = Http::post("{$this->storageBase}/download/single/id", $payload);
        if (!$StorageResponse->successful()) {
            return response()->json(['success' => false, 'message' => 'Storage Service Failed', 'error' => $StorageResponse->body()], 500);
        }
        $storageData = $StorageResponse->json();
        $storageData['encryptedKey'] = $encryptedKey;
        $storageData['ownerPublicKey'] = $owner->public_key;


        return response()->json($storageData, 200);

    }
    public function getFileMetadata(Request $request){
        $validated = $request->validate([
            "fileId" => "required|string|exists:files,id",
        ]);

        $file = File::with('currentVersion')->find($validated['fileId']);

        if (!$file || $file->is_deleted) {
            return response()->json([
                "success" => false,
                "message" => "File not found or has been deleted.",
            ], 404);
        }
        return response()->json([
            "success" => true,
            "data" => [
                "id" => $file->id,
                "name" => $file->name,
                "mime_type" => $file->mime_type,
                "size_bytes" => $file->size_bytes,
                "cid" => $file->cid,
                "file_hash" => $file->file_hash,
                "encryption_algorithm" => $file->encryption_algorithm,
                "iv" => $file->iv,
                "owner_encrypted_key" => $file->owner_encrypted_key,
                "created_at" => $file->created_at,
                "updated_at" => $file->updated_at,
                "current_version" => $file->currentVersion,
            ]
        ]);
    }

}
