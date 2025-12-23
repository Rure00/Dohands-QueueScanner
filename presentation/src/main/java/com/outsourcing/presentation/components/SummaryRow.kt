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
import com.outsourcing.presentation.state.JobSummaryUiModel

@Composable
fun SummaryRow(
    summary: JobSummaryUiModel,
    forceOffline: Boolean,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(modifier = modifier) {
        Column(Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SummaryChip("PENDING: ${summary.pending}")
                Spacer(Modifier.width(8.dp))
                SummaryChip("SENT: ${summary.sent}")
                Spacer(Modifier.width(8.dp))
                SummaryChip("FAILED: ${summary.failed}")
                Spacer(Modifier.weight(1f))
                val badgeText = if (forceOffline) "OFFLINE: ON" else "OFFLINE: OFF"
                AssistChip(
                    onClick = {},
                    label = { Text(badgeText) },
                    enabled = false
                )
            }
        }
    }
}

@Composable
private fun SummaryChip(text: String) {
    AssistChip(
        onClick = {},
        label = { Text(text) },
        enabled = false
    )
}