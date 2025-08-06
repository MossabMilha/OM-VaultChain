<?php

namespace App\Http\Controllers;


use App\Models\User;
use Illuminate\Http\Request;

class UserController extends Controller
{
    public function getPublicInformation(Request $request){
        $validated = $request->validate([
            'userId' => 'required|uuid'
        ]);
        $user = User::where('id',$validated['userId'])->where('is_active',1)->first();
        if(!$user){
            return response()->json(['error' => 'User not found'], 404);
        }
        return  response()->json([
            "userId"=>$user->id,
            "firstName"=>$user->first_name,
            "lastName"=>$user->last_name,
            "email"=>$user->email,
            "publicKey"=>$user->public_key,
        ]);
    }
    public function search(Request $request)
    {
        $query = $request->input('username');

        if (!$query) {
            return response()->json([], 200);
        }

        return User::where('username', 'like', "%{$query}%")
            ->select('id', 'username')
            ->limit(10)
            ->get();
    }
}
