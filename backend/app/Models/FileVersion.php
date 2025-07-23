<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class FileVersion extends Model
{
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
}
