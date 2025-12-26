package com.outsourcing.presentation.intent

import com.outsourcing.domain.entities.Job

sealed class JobIntent {
    data class InquireJob(val rawBarcode: String): JobIntent()
    data class SendOrAddJob(val job: Job): JobIntent()
    data class SendJob(val job: Job): JobIntent()
    data object SendPendingJobs: JobIntent()
    data object SendFailJobs: JobIntent()
}