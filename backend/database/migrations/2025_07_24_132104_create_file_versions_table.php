<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('file_versions', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->uuid('file_id');
            $table->integer('version_number');
            $table->string('cid');
            $table->string('file_hash');
            $table->boolean('is_current')->default(false);
            $table->timestamp('created_at')->useCurrent();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('file_versions');
    }
};
