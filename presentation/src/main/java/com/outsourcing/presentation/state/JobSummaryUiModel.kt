package com.outsourcing.presentation.state

data class JobSummaryUiModel(
    val pending: Int,
    val sent: Int,
    val failed: Int,
)