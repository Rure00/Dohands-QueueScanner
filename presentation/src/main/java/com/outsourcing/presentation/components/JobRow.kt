package com.outsourcing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outsourcing.presentation.state.JobEventUiModel
import com.outsourcing.presentation.state.JobStatus

@Composable
fun JobRow(
    job: JobEventUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val statusText = when (job.status) {
        JobStatus.PENDING -> "PENDING"
        JobStatus.SENT -> "SENT"
        JobStatus.FAILED -> "FAILED"
    }

    ElevatedCard(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        job.barcode,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        job.timeText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666)
                    )
                }
                StatusPill(statusText)
            }

            if (job.status == JobStatus.FAILED && !job.errorText.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    job.errorText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun StatusPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = MaterialTheme.typography.labelMedium)
    }
}