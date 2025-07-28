<?php

namespace App\Http\Controllers;

use App\Models\User;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Str;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    public function signupBackupCode(Request $request){
        try {
            $validated = $request->validate([
                "firstName" => "required|string|max:255",
                "lastName" => "required|string|max:255",
                "email" => "required|email|unique:users,email",
                "password" => "required|string|min:8",
                "publicKey" => "required|string|max:5000",
                "encryptedPrivateKey" => "required|string|max:10000",
                "iv" => "required|string|max:255",
            ]);
            if (!User::validatePassword($validated["password"])){
                return response()->json([
                    "success" => false,
                    "message" => "Password must have uppercase, lowercase, number, and symbol."
                ], 422);
            }

            $userid = str::uuid();
            $walletAddress = "0x" . substr(str::uuid(), 0, 40);

            $user = User::create([
                "id" => $userid,
                "first_name" => $validated["firstName"],
                "last_name" => $validated["lastName"],
                "email" => $validated["email"],
                "password" => bcrypt($validated["password"]),
                "wallet_address" => $walletAddress,
                "public_key" => $validated["publicKey"],
                "encrypted_private_key" => $validated["encryptedPrivateKey"],
                "iv" => $validated["iv"]
            ]);
            return response()->json([
                "success" => true,
                "message" => "User registered successfully",
                "userId" => $userid,
                "walletAddress" => $walletAddress
            ], 201);


        } catch (ValidationException $e) {
            return response()->json([
                "success" => false,
                "message" => "Validation failed",
                "errors" => $e->errors()
            ], 422);
        } catch (Exception $e) {
            return response()->json([
                "success" => false,
                "message" => "Signup failed",
                "error" => $e->getMessage()
            ], 500);
        }
    }
    
}
