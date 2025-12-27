package com.outsourcing.domain.repository

import com.outsourcing.domain.entities.Job
import kotlinx.coroutines.flow.Flow

interface LocalJobRepository {
    fun observeJobById(id: String): Flow<Job?>

    fun collectLocalJobs(): Flow<List<Job>>

    suspend fun updateJob(job: Job): Result<Job>

    suspend fun getSentJob(): Result<List<Job>>
    suspend fun getPendingJob(): Result<List<Job>>
    suspend fun getFailedJob(): Result<List<Job>>

    suspend fun addJob(job: Job): Result<Boolean>
    suspend fun deleteJob(job: Job): Result<Boolean>
}