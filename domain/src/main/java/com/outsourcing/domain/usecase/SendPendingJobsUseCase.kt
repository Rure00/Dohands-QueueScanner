package com.outsourcing.domain.usecase

import com.outsourcing.domain.repository.JobRepository
import com.outsourcing.domain.repository.LocalJobRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendPendingJobsUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    private val localJobRepository: LocalJobRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(ioDispatcher) {
        supervisorScope {
            localJobRepository.getPendingJob().getOrElse { listOf() }.map {
                async {
                    jobRepository.sendJob(it)
                }
            }.awaitAll()
        }
    }
}