<?php

namespace App\Services;



use App\Models\User;
use Illuminate\Support\Str;
use Illuminate\Validation\ValidationException;

class AuthService
{
    public function register(array $data): User
    {
        if (!User::validatePassword($data["password"])) {
            throw ValidationException::withMessages([
                'password' => ['Password must have uppercase, lowercase, number, and symbol.']
            ]);
        }

        $userId = Str::uuid();

        return User::create([
            "id" => $userId,
            "first_name" => $data["firstName"],
            "last_name" => $data["lastName"],
            "email" => $data["email"],
            "password" => bcrypt($data["password"]),
            "wallet_address" => $data["walletAddress"],
            "public_key" => $data["publicKey"],
            "encrypted_private_key" => $data["encryptedPrivateKey"],
            "iv" => $data["iv"],
            "signup_method" => $data["signup_method"]
        ]);
    }
}
