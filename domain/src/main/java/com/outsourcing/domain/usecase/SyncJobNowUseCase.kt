package com.outsourcing.domain.usecase

import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.entities.JobStatus
import com.outsourcing.domain.repository.JobRepository
import com.outsourcing.domain.repository.LocalJobRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncJobNowUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    private val localJobRepository: LocalJobRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() =  withContext(ioDispatcher) {
        val pendingJobs = localJobRepository.getPendingJob().getOrElse { listOf() }
        val sentJobs = localJobRepository.getSentJob().getOrElse { listOf() }

        (pendingJobs + sentJobs).map {
            jobRepository.sendJob(it).getOrNull()
                ?: it.copy(retryCount = it.retryCount + 1, status = JobStatus.FAILED)
        }
    }
}