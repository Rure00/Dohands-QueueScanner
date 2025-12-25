package com.rure.barcode_scanner

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner

fun createCameraController(context: Context) = CameraControllerImpl(context)

interface CameraController {
    fun getPreviewView(): View
    fun startCamera(lifecycleOwner: LifecycleOwner)

    suspend fun takePhoto(): String

    fun unbind()
}