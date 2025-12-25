package com.outsourcing.domain.entities

import java.time.LocalDateTime

data class Job(
    val id: String,
    val barcode: String,
    val time: LocalDateTime,
    val status: JobStatus,
    val errorText: String? = null,
    val retryCount: Int = 0
)
