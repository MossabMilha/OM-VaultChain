<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Services\AuthService;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Str;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    private AuthService $authService;
    public function __construct(AuthService $authService){$this->authService = $authService;}

    public function signup(Request $request){
        try {
            $validated = $request->validate([
                "firstName" => "required|string|max:255",
                "lastName" => "required|string|max:255",
                "email" => "required|email|unique:users,email",
                "password" => "required|string|min:8",
                "publicKey" => "required|string|max:5000",
                "encryptedPrivateKey" => "required|string|max:10000",
                "iv" => "required|string|max:255",
                "walletAddress" => "required|string|max:255",
                "signup_method" => "required|string|in:wallet,generated",
            ]);


            $user = $this->authService->register($validated);

            return response()->json([
                "success" => true,
                "message" => "User registered successfully",
                "userId" => $user->id,
                "walletAddress" => $user->wallet_address
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

    public function login(Request $request){
        $validated = $request->validate([
            "email" => "required|email",
            "password" => "required|string|min:8",
        ]);

        $user = User::where("email", $validated["email"])->first();

        if (!$user || !password_verify($validated["password"], $user->password)) {
            return response()->json([
                "success" => false,
                "message" => "Invalid email or password"
            ], 401);
        }

        $token = $user->createToken("auth_token")->plainTextToken;

        return response()->json([
            "success" => true,
            "message" => "Login successful",
            "data" => [
                "userId" => $user->id,
                "firstName" => $user->first_name,
                "lastName" => $user->last_name,
                "email" => $user->email,
                "walletAddress" => $user->wallet_address,
                "publicKey" => $user->public_key,
                "encryptedPrivateKey" => $user->encrypted_private_key,
                "iv" => $user->iv,
                "signupMethod" => $user->signup_method,

            ],
            "token" => $token
        ], 200);
    }

}
