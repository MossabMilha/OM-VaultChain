<?php

namespace App\Http\Controllers;


use App\Models\AccessPermission;
use App\Models\File;
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
    public function getUserWithAccessInfo(Request $request)
    {
        $validated = $request->validate([
            'fileId' => 'required|uuid',
            'userId' => 'required|uuid'
        ]);

        $file = File::findOrFail($validated['fileId']);

        if ($file->owner_id !== $validated['userId']) {
            return response()->json([
                'error' => 'You are not the owner of this file.'
            ], 403);
        }

        $usersWithAccess = AccessPermission::active()
            ->where('file_id', $validated['fileId'])
            ->where('user_id', '!=', $file->owner_id)
            ->with(['user:id,first_name,last_name,username,email,wallet_address,public_key'])
            ->get()
            ->pluck('user'); // Get only the related users

        return response()->json([
            'users' => $usersWithAccess
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
