<?php

use App\Http\Controllers\AccessController;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\FileOrchestrationController;
use App\Http\Controllers\UserController;

use Illuminate\Support\Facades\Route;

// Test route for CORS


Route::post('/auth/signup', [AuthController::class, 'signup']);
Route::middleware('web')->post('/auth/login', [AuthController::class, 'login']);

Route::post('/files/metadata', [FileOrchestrationController::class, 'getFileMetadata']);
Route::post('/files/list', [FileOrchestrationController::class, 'listFiles']);
Route::post('/files/list/owned', [FileOrchestrationController::class, 'listOwnedFiles']);
Route::post('/files/upload/single', [FileOrchestrationController::class, 'uploadSingleFile']);
Route::post('/files/upload/batch', [FileOrchestrationController::class, 'uploadBatchFile']);
Route::post('/files/download/single', [FileOrchestrationController::class, 'downloadSingleFile']);


Route::post('/user/publicInformation', [UserController::class, 'getPublicInformation']);

Route::post('/access/grantAccess', [AccessController::class, 'grantAccessSingleUser']);


