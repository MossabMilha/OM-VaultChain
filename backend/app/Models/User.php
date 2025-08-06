<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class User extends Authenticatable
{
    use HasApiTokens, HasFactory, Notifiable;

    public $incrementing = false;
    protected $keyType = 'string'; // Since id is CHAR(36) (UUID)

    /**
     * The attributes that are mass assignable.
     *
     * @var array<int, string>
     */
    protected $fillable = [
        'id',
        'first_name',
        'last_name',
        'username',
        'email',
        'password',
        'wallet_address',
        'public_key',
        'encrypted_private_key',
        'iv',
        'signup_method',
        'is_active',
    ];

    /**
     * The attributes that should be hidden for serialization.
     *
     * @var array<int, string>
     */
    protected $hidden = [
        'password',
        'remember_token',
        'encrypted_private_key',
        'iv',
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array<string, string>
     */
    protected function casts(): array
    {
        return [
            'email_verified_at' => 'datetime',
            'password' => 'hashed',
            'is_active' => 'boolean',
        ];
    }

    /**
     * Validate password strength.
     */
    public static function validatePassword(string $password): bool
    {
        return strlen($password) >= 8 &&
            preg_match('/[A-Z]/', $password) &&    // at least one uppercase
            preg_match('/[a-z]/', $password) &&    // at least one lowercase
            preg_match('/[0-9]/', $password) &&    // at least one number
            preg_match('/[\W_]/', $password);      // at least one special char
    }
}
