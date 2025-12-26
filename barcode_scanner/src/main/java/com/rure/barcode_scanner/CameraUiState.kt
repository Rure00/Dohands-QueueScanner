package com.rure.barcode_scanner

sealed class CameraUiState {
    data object NotReady: CameraUiState()
    data object Ready: CameraUiState()

    data object Scanning: CameraUiState()
    data class Captured(val imageUri: String): CameraUiState()
}