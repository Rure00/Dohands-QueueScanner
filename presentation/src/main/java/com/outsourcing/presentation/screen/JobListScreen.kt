package com.outsourcing.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.entities.JobStatus
import com.outsourcing.presentation.components.JobRow
import com.outsourcing.presentation.components.SummaryRow
import com.outsourcing.presentation.state.JobSummaryUiModel
import com.outsourcing.presentation.viewmodels.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(
    jobViewModel: JobViewModel = hiltViewModel()
) {
    val jobs by jobViewModel.jobs.collectAsState()
    val summary by remember {
        derivedStateOf {
            val arr = Array(3) { 0 }

            jobs.forEach {
                when (it.status) {
                    JobStatus.PENDING -> arr[0] += 1
                    JobStatus.SENT -> arr[1] += 1
                    JobStatus.FAILED -> arr[2] += 1
                }
            }

            JobSummaryUiModel(arr[0], arr[1], arr[2])
        }
    }
    val isOffline by jobViewModel.isOffline.collectAsState()

    var selectedStatus by remember { mutableStateOf<JobStatus>(JobStatus.PENDING) }


    val onSyncNow: () -> Unit = {

    }
    val onClickJob: (Job) -> Unit= {

    }


    // =============================================================================



    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SummaryRow(
            selected = selectedStatus,
            summary = summary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = { selectedStatus = it }
        )

        Button(
            onClick = onSyncNow,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("SYNC NOW")
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "Work Events",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            style = MaterialTheme.typography.titleSmall,
            color = Color(0xFF444444)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(jobs, key = { it.id }) { job ->
                JobRow(
                    job = job,
                    onClick = { onClickJob(job) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}
