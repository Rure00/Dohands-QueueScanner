package com.outsourcing.presentation.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outsourcing.domain.entities.Job
import com.outsourcing.presentation.components.DetailRow
import com.outsourcing.presentation.components.ErrorChip
import com.outsourcing.presentation.ui.theme.Black
import com.outsourcing.presentation.viewmodels.JobDetailViewModel
import com.outsourcing.presentation.viewmodels.JobViewModel

@Composable
fun JobDetailScreen(
    jobId: String,
    jobDetailViewModel: JobDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val job by jobDetailViewModel.jobFlow.collectAsStateWithLifecycle()
    val onCopyEventId: (String?) -> Unit = {
        if (!it.isNullOrEmpty()) {
            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("JobId", it))
        }
    }

    // ============================================================================================


    LaunchedEffect(jobId) {
        jobDetailViewModel.observeJobById(jobId)
    }



    // ============================================================================================

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        Text(
            text = "Event Detail",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = Black
        )

        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))

        DetailRow(label = "EVENT ID:", value = job?.id ?: "")
        Spacer(Modifier.height(10.dp))
        DetailRow(label = "RETRY COUNT:", value = job?.retryCount.toString() )

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))

        Text(
            text = "BarCode",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Black
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = job?.barcode ?: "",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Black
        )

        Spacer(Modifier.height(16.dp))

        job?.errorText?.let {
            ErrorChip(text = it)
            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { onCopyEventId(job?.id) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("COPY EVENT ID")
        }
    }
}

