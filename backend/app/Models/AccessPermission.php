<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class AccessPermission extends Model
{
    use HasUuids;
    protected $table = 'access_permissions';
    public $incrementing = false;
    protected $keyType = 'string';
    protected $fillable = [
        "id",
        "file_id",
        "user_id",
        "encrypted_key",
        "granted_at",
        "revoked_at",
        "is_active",
    ];
    protected $casts = [
        'is_active' => 'boolean',
        'granted_at' => 'datetime',
        'revoked_at' => 'datetime',
    ];
    public function file(): BelongsTo{
        return $this->belongsTo(File::class, 'file_id');
    }
    public function user(): BelongsTo{
        return $this->belongsTo(User::class, 'user_id');
    }
    public function scopeActive($query){
        return $query->where('is_active', true)->whereNull('revoked_at');
    }
}
