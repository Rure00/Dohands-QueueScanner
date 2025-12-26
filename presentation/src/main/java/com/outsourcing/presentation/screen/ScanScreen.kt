package com.outsourcing.presentation.screen

import android.Manifest
import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Toast
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.outsourcing.presentation.components.ForceOfflineCard
import com.outsourcing.presentation.viewmodels.JobViewModel
import com.rure.barcode_scanner.CameraUiState
import com.rure.barcode_scanner.createCameraController

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    jobViewModel: JobViewModel = hiltViewModel(),
    toJobListScreen: () -> Unit,
) {
    val appContext = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraController = remember { createCameraController(appContext) }
    val cameraState by cameraController.cameraState.collectAsState()

    val isOffline by jobViewModel.isOffline.collectAsState()

    val onToggleForceOffline: (Boolean) -> Unit = {

    }
    val onMockScan: () -> Unit = {

    }

    val permissionState = rememberMultiplePermissionsState(listOf(Manifest.permission.CAMERA)) {
        if(!it.containsValue(false)) {
            cameraController.startCamera(lifecycleOwner)
        } else {
            Toast.makeText(appContext, "앱을 사용하기 위해선 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // =============================================================================

    DisposableEffect(permissionState) {
        permissionState.launchMultiplePermissionRequest()

        onDispose {
            cameraController.unbind()
        }
    }


    // =============================================================================


    Column(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxSize()
    ) {
        ForceOfflineCard(
            forceOffline = isOffline,
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
                        .fillMaxSize(0.9f),
                ) {
                    Button(
                        onClick = { permissionState.launchMultiplePermissionRequest() }
                    ) {
                        Text(text = "권한 허용하기")
                    }
                }
            } else {
                AndroidView(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize(0.9f),
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