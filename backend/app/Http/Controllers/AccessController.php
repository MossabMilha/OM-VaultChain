<?php

namespace App\Http\Controllers;

use App\Models\AccessPermission;
use App\Models\File;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;

class AccessController extends Controller
{
    private $storageBase;
    private $blockchainBase;

    public function __construct()
    {
        $this->storageBase = config('services.storage_service.base_url');
        $this->blockchainBase = config('services.blockchain_service.base_url');
    }
    public function grantAccessSingleUser(Request $request){
        $validated = $request->validate([
            "fileId" => "required|uuid|exists:files,id",
            "userId" => "required|uuid|exists:users,id",
            "encryptedKey" => "required|string",
        ]);
        $file = File::where('id', $validated["fileId"])->firstOrFail();
        $user = User::where('id', $validated["userId"])->firstOrFail();

        $payload = [
            "cid"            => $file->cid,
            "granteeWallet"  => $user->wallet_address,
            "encryptedKey"   => $validated["encryptedKey"],
        ];

        $BlockchainResponse = Http::post("{$this->blockchainBase}/grant-access", $payload);

        if (!$BlockchainResponse->successful()) {
            return response()->json([
                'success' => false,
                'message' => 'Blockchain service failed',
                'error' => $BlockchainResponse->body(),
            ], 500);
        }
        $txHash = $BlockchainResponse->json('transactionHash');
        $accessPermission = AccessPermission::updateOrCreate(
            [
                'file_id' => $validated['fileId'],
                'user_id' => $validated['userId'],
            ],
            [
                'encrypted_key' => $validated['encryptedKey'],
                'tx_hash' => $txHash,
                'granted_at' => now(),
                'revoked_at' => null,
                'is_active' => true,
            ]
        );
        return response()->json([
            'success' => true,
            'message' => 'Access granted successfully',
            'data' => [
                'accessPermission' => $accessPermission,
                'transactionHash' => $txHash,
            ],
        ]);

    }
}
