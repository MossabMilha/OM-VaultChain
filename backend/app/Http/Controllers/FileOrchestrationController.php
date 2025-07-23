<?php

namespace App\Http\Controllers;

use App\Models\FileVersion;
use Illuminate\Auth\Events\Validated;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Str;

class FileOrchestrationController extends Controller
{
    public function uploadSingleFile(Request $request){
        if ($request->isMethod('options')) {
            return response()->json([], 200)
                ->header('Access-Control-Allow-Origin', 'http://localhost:5173')
                ->header('Access-Control-Allow-Methods', 'POST, OPTIONS')
                ->header('Access-Control-Allow-Headers', 'Content-Type, Accept');
        }

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
            return response()->json([
                'success' => false,
                'message' => 'Storage service failed',
                'error' => $storageResponse->body()
            ], 500);
        }
        $storageData = $storageResponse->json();
        $fileId = $storageData['fileId'] ?? null;
        $cid = $storageData['cid'] ?? null;
        $fileHash = $validated["metadata"]["hash"];
        if(!$fileId || !$cid){
            return response()->json([
                'success' => false,
                'message' => 'Storage Service Did Not Return A Valid File ID or CID',
                'error' => $storageData
            ], 500);
        }
        FileVersion::create([
            "id" => Str::uuid(),
            "file_id" => $fileId,
            "version_number" => 1,
            "cid" => $cid,
            "file_hash" => $fileHash,
            "is_current" => true
        ]);

        // 🏗️ Prepare blockchain data
        $cid = $storageData['cid'] ?? null;
        if (!$cid) {
            return response()->json([
                'success' => false,
                'message' => 'Storage Service Did Not Return A Valid CID',
                'error' => $storageData
            ], 500);
        }
        $blockchainResponse = Http::post("{$blockchainBase}/register-file", [
            "cid" => $cid,
            "fileHash" => $validated["metadata"]["hash"],
        ]);
        if (!$blockchainResponse->successful()) {
            return response()->json([
                'success' => false,
                'message' => 'Blockchain Service Failed',
                'error' => $blockchainResponse->body()
            ], 500);
        }
        $blockchainData = $blockchainResponse->json();
        $finalData = $storageData + $blockchainData;
        return response()->json([
            "success" => true,
            "message" => "File Uploaded Successfully",
            "data" => $finalData
        ]);

    }
}

