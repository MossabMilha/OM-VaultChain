<?php

use App\Http\Controllers\FileOrchestrationController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\FileController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/
Route::post('/files/upload', [FileOrchestrationController::class, 'uploadSingleFile']);
Route::post('/files/batch', [FileOrchestrationController::class, 'uploadBatchFile']);

