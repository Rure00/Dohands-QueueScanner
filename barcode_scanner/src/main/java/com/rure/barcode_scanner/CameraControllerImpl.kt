package com.rure.barcode_scanner

import android.content.Context
import android.view.View
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.core.Preview
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraControllerImpl(
    private val context: Context
): CameraController {
    private val _cameraState = MutableStateFlow<CameraUiState>(CameraUiState.NotReady)
    override val cameraState: StateFlow<CameraUiState> = _cameraState.asStateFlow()

    private lateinit var cameraController: LifecycleCameraController
    private lateinit var previewView: PreviewView
    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var cameraExecutor: ExecutorService


    override fun getPreviewView(): View  = previewView

    override fun startCamera(lifecycleOwner: LifecycleOwner) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_CODABAR)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)
        previewView = PreviewView(context)

        val mainExecutor = ContextCompat.getMainExecutor(context)

        cameraController = LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
            setImageAnalysisAnalyzer(
                mainExecutor,
                MlKitAnalyzer(
                    listOf(barcodeScanner),
                    COORDINATE_SYSTEM_VIEW_REFERENCED,
                    mainExecutor
                ) { result: MlKitAnalyzer.Result? ->
                    val barcodeResults = result?.getValue(barcodeScanner)   // result
                    if ((barcodeResults == null) ||
                        (barcodeResults.size == 0) ||
                        (barcodeResults.first() == null)
                    ) {
                        // previewView.overlay.clear()
                        // previewView.setOnTouchListener { _, _ -> false } //no-op
                        return@MlKitAnalyzer
                    }
                }
            )
        }

        previewView.controller = cameraController
        cameraExecutor = Executors.newSingleThreadExecutor()
        _cameraState.value = CameraUiState.Ready
    }

    override suspend fun takePhoto(): String {
        TODO("Not yet implemented")
    }

    override fun unbind() {
        cameraController.unbind()
        cameraExecutor.shutdown()
        barcodeScanner.close()
    }
}