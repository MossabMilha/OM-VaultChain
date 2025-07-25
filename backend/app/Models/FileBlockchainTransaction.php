<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class FileBlockchainTransaction extends Model
{
    use HasUuids;
    protected $table = 'file_blockchain_transactions';
    protected $fillable = [
        "file_version_id",
        "tx_hash",
        "block_number",
        "chain_id",
        "tx_status",
    ];
    const UPDATED_AT = null;

    public function fileVersion():BelongsTo{
        return $this->belongsTo(FileVersion::class);
    }
}
