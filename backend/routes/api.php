<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\FileOrchestrationController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

// Test route for CORS


Route::post('/auth/signup/backupCode', [AuthController::class, 'signupBackupCode']);
Route::post('/auth/signup/wallet', [AuthController::class, 'signupWallet']);






Route::post('/files/list', [FileOrchestrationController::class, 'listFiles']);

Route::post('/files/upload/single', [FileOrchestrationController::class, 'uploadSingleFile']);
Route::post('/files/upload/batch', [FileOrchestrationController::class, 'uploadBatchFile']);

Route::post('/files/download/single', [FileOrchestrationController::class, 'downloadSingleFile']);




