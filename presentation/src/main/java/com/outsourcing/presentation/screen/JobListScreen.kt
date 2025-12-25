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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.outsourcing.domain.entities.Job
import com.outsourcing.presentation.components.JobRow
import com.outsourcing.presentation.components.SummaryRow
import com.outsourcing.presentation.state.JobSummaryUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(
) {
    val jobs: List<Job> = listOf()
    val summary: JobSummaryUiModel = JobSummaryUiModel(0, 0, 0)
    val forceOffline: Boolean = false


    val onBack: () -> Unit = {

    }
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
            summary = summary,
            forceOffline = forceOffline,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
