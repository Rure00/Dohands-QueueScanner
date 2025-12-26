package com.outsourcing.domain.repository

import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.entities.RemoteResult
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    suspend fun sendJob(job: Job): RemoteResult<Job>
}