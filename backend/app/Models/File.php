<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class File extends Model
{
    public $incrementing = false;
    protected $keyType = 'string';
    protected $table = 'files';
    protected $fillable = [
        'id',
        'owner_id',
        'name',
        'mime_type',
        'size_bytes',
        'cid',
        'file_hash',
        'created_at',
        'updated_at',
        'is_deleted',
        'iv',
        'owner_encrypted_key',
        'encryption_algorithm',
    ];
    public $timestamps = true;
    protected $casts = [
        'is_deleted' => 'boolean',
        'created_at' => 'datetime',
        'updated_at' => 'datetime',
    ];
    public function versions(): HasMany
    {
        return $this->hasMany(FileVersion::class, 'file_id');
    }

    public function currentVersion()
    {
        return $this->hasOne(FileVersion::class, 'file_id')->where('is_current', true);
    }
}
