package com.outsourcing.presentation.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.outsourcing.domain.entities.JobStatus
import com.outsourcing.presentation.state.JobSummaryUiModel
import com.outsourcing.presentation.ui.theme.Black
import com.outsourcing.presentation.ui.theme.White

@Composable
fun SummaryRow(
    selected: JobStatus,
    summary: JobSummaryUiModel,
    modifier: Modifier = Modifier,
    onClick: (JobStatus) -> Unit,
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors().copy(containerColor = Color.LightGray)
    ) {
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SummaryChip(
                    status = JobStatus.PENDING,
                    num = summary.pending,
                    isSelected = selected == JobStatus.PENDING,
                    modifier = Modifier.weight(1f)
                ) {
                    onClick(JobStatus.PENDING)
                }

                SummaryChip(
                    status = JobStatus.SENT,
                    num = summary.sent,
                    isSelected = selected == JobStatus.SENT,
                    modifier = Modifier.weight(1f)
                ) {
                    onClick(JobStatus.SENT)
                }

                SummaryChip(
                    status = JobStatus.FAILED,
                    num = summary.failed,
                    isSelected = selected == JobStatus.FAILED,
                    modifier = Modifier.weight(1f)
                ) {
                    onClick(JobStatus.FAILED)
                }
            }
        }
    }
}

@Composable
private fun SummaryChip(
    status: JobStatus,
    num: Int,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: (JobStatus) -> Unit
) {
    val text = "${status.name}: $num"
    val containerColor = if (isSelected) CardDefaults.cardColors().containerColor
                        else Color.LightGray
    Box(
        modifier = modifier
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onClick(status) },
            )
            .background(color = containerColor, AssistChipDefaults.shape)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = White
        )
    }
}