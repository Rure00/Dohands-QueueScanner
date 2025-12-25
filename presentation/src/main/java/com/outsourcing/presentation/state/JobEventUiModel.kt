package com.outsourcing.presentation.state

import com.outsourcing.domain.entities.JobStatus

data class JobEventUiModel(
    val id: String,
    val barcode: String,
    val timeText: String,
    val status: JobStatus,
    val errorText: String? = null,
    val retryCount: Int = 0
)