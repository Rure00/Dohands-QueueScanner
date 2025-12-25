package com.outsourcing.presentation.screen

import android.util.Log
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.outsourcing.presentation.components.ForceOfflineCard
import com.rure.barcode_scanner.CameraUiState
import com.rure.barcode_scanner.createCameraController

@Composable
fun ScanScreen(
    toJobListScreen: () -> Unit,
) {
    val appContext = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraController = remember { createCameraController(appContext) }


    val cameraState by cameraController.cameraState.collectAsState()

    var forceOffline by remember { mutableStateOf(false) }
    val onToggleForceOffline: (Boolean) -> Unit = {

    }
    val onMockScan: () -> Unit = {

    }

    // =============================================================================


    DisposableEffect(true) {
        cameraController.startCamera(lifecycleOwner)

        onDispose {
            cameraController.unbind()
        }
    }

    LaunchedEffect(cameraState) {
        Log.d("ScanScreen", "CameraState: ${cameraState}")
    }





    // =============================================================================


    Column(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxSize()
    ) {
        ForceOfflineCard(
            forceOffline = forceOffline,
            onToggle = onToggleForceOffline,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Camera preview placeholder (여기에 CameraX PreviewView/Analyzer를 붙이면 됨)
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEDEDED)),
            contentAlignment = Alignment.Center
        ) {
            // Scan frame overlay
//            Spacer(
//                modifier = Modifier
//                    .aspectRatio(1f)
//                    .fillMaxWidth()
//                    .background(Color.Gray),
//            )
//
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text("Scanning...", fontSize = 16.sp, color = Color(0xFF444444))
//                Spacer(Modifier.height(8.dp))
//                Text(
//                    "Camera preview goes here",
//                    fontSize = 12.sp,
//                    color = Color(0xFF666666)
//                )
//            }

            if (cameraState == CameraUiState.NotReady) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize(0.7f),
                )
            } else {
                AndroidView(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize(0.7f),
                    factory = {
                        cameraController.getPreviewView()
                    }
                )
            }

        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onMockScan,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Mock Scan")
        }

        Spacer(Modifier.height(16.dp))
    }
}