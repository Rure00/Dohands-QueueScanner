package com.outsourcing.domain.repository

import com.outsourcing.domain.entities.Job

interface LocalJobRepository {
    suspend fun getJob(): Result<List<Job>>
    suspend fun addJob(job: Job): Result<Job>
    suspend fun updateJob(job: Job): Result<Job>
    suspend fun deleteJob(job: Job): Result<Job>
}