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
    private val sendJobUseCase: SendJobUseCase,
    private val jobRepository: JobRepository,
    private val localJobRepository: LocalJobRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(tryUntil: Int) = withContext(ioDispatcher) {
        supervisorScope {
            localJobRepository.getPendingJob().getOrElse { listOf() }.map {
                async { sendJobUseCase(it, tryUntil) }
            }.awaitAll()
        }
    }
}