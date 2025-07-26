<?php

namespace App\Http\Controllers;

use App\Models\File;
use App\Models\FileBlockchainTransaction;
use App\Models\FileVersion;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Str;

class FileOrchestrationController extends Controller
{
    public function uploadSingleFile(Request $request){

        $storageBase = config('services.storage_service.base_url');
        $blockchainBase = config('services.blockchain_service.base_url');

        $validated = $request->validate([
            "encryptedFile"=>"required|string",
            "metadata.name"=>"required|string",
            "metadata.size"=>"required|integer",
            "metadata.mimeType"=>"required|string",
            "metadata.hash"=>"required|string",
            "metadata.encryptionKey"=>"required|string",
            "metadata.ownerId"=>"required|uuid",
            "metadata.iv" => "required|string"
        ]);

        $payload = [
            "fileData" => $validated["encryptedFile"],
            "fileName" => $validated["metadata"]["name"],
            "mimeType" => $validated["metadata"]["mimeType"],
            "ownerId" => $validated["metadata"]["ownerId"],
            "iv" => $validated["metadata"]["iv"],
            "encryptedKey" => $validated["metadata"]["encryptionKey"],
            "fileHash" => $validated["metadata"]["hash"]
        ];

        // 🔁 Send to storage-service
        $storageResponse = Http::post("{$storageBase}/upload/single", $payload);
        if (!$storageResponse->successful()) {
            return response()->json(['success' => false, 'message' => 'Storage service failed', 'error' => $storageResponse->body()], 500);
        }

        $storageData = $storageResponse->json();

        // Create file version
        $fileId = $storageData['fileId'];
        $cid = $storageData['cid'];
        $fileHash = $validated["metadata"]["hash"];

        $fileVersion = FileVersion::create([
            "id" => Str::uuid(),
            "file_id" => $fileId,
            "version_number" => 1,
            "cid" => $cid,
            "file_hash" => $fileHash,
            "is_current" => true
        ]);

        // 🏗️ Prepare blockchain data


        $blockchainResponse = Http::post("{$blockchainBase}/register-file", [
            "uploaderWallet" => "0x3598b9413498353D666cA08367c602982DCc4931",
            "cid" => $cid,
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

        $storageBase = config('services.storage_service.base_url');
        $blockchainBase = config('services.blockchain_service.base_url');


        $validated = $request->validate([
            "ownerId" => "required|uuid",
            "files" => "required|array",
            "files.*.fileName" => "required|string",
            "files.*.mimeType" => "required|string",
            "files.*.sizeBytes" => "required|integer",
            "files.*.iv" => "required|string",
            "files.*.fileHash" => "required|string",
            "files.*.encryptedKey" => "required|string",
            "files.*.fileData" => "required|string" // if you're passing base64 or similar
        ]);

        $payloads =[
            "ownerId" => $validated["ownerId"],
            "files" => $validated["files"]
        ];
        $storageResponse = Http::post("{$storageBase}/upload/batch", $payloads);
        if (!$storageResponse->successful()) {
            return response()->json(['success' => false, 'message' => 'Storage service failed', 'error' => $storageResponse->body()], 500);
        }
        $storageData = $storageResponse->json();

        $fileVersions = [];
        $blockchainResponses = [];

        foreach ($storageData as $index => $file) {
            // Note: BatchUploadResponse doesn't include fileId, so we query by CID
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
            $blockchainResponse = Http::post("{$blockchainBase}/register-file", [
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
    public function listFiles(Request $request){
        $validated = $request->validate([
            "ownerId" => "required|uuid"
        ]);
        $files = File::where('owner_id', $validated['ownerId'])->get();

        return response()->json(["files"=>$files], 200);
    }

    public function downloadSingleFile(Request $request){

    }


}
