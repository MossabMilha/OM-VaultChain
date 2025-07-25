<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class FileVersion extends Model
{
    use HasFactory;
    protected $table = 'file_versions';
    public $incrementing = false;
    protected $keyType = 'string';
    protected $fillable = [
        "id",
        "file_id",
        "version_number",
        "cid",
        "file_hash",
        "is_current"
    ];
    public $timestamps = false;
    protected $casts = [
        "is_current" => "boolean",
        "created_at" => "datetime",
    ];
    public function file():BelongsTo{
        return $this->belongsTo(File::class, 'file_id', 'id');
    }
    public function blockchainTransactions():HasMany
    {
        return $this->hasMany(FileBlockchainTransaction::class);
    }
}
