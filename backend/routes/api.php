<?php

use App\Http\Controllers\FileOrchestrationController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

// Test route for CORS
Route::get('/test', function () {
    return response()->json(['message' => 'CORS is working!', 'timestamp' => now()]);
});

Route::post('/files/upload', [FileOrchestrationController::class, 'uploadSingleFile']);
Route::post('/files/batch', [FileOrchestrationController::class, 'uploadBatchFile']);
