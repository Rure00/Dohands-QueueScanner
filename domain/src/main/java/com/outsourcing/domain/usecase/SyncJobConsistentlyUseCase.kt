package com.outsourcing.domain.usecase

import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.entities.JobStatus
import com.outsourcing.domain.repository.JobRepository
import com.outsourcing.domain.repository.LocalJobRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SyncJobConsistentlyUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    private val localJobRepository: LocalJobRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke() = localJobRepository
        .collectLocalJobs()
        .map { it.filter { job -> job.status != JobStatus.SENT } }
        .distinctUntilChanged()
        .mapLatest {
            supervisorScope {
                it.map { job ->
                    async(ioDispatcher) {
                        jobRepository.sendJob(job).getOrNull()
                            ?: job.copy(retryCount = job.retryCount + 1, status = JobStatus.FAILED)
                    }
                }.awaitAll()
            }
        }
        .flowOn(ioDispatcher)
}