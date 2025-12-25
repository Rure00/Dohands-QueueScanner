package com.outsourcing.domain.repository

import com.outsourcing.domain.entities.Job
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    suspend fun syncJob(): Flow<Job>
}