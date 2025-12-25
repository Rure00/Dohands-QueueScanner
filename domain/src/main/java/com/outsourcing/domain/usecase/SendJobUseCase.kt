package com.outsourcing.domain.usecase

import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.repository.JobRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendJobUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(job: Job) = withContext(ioDispatcher) {
        jobRepository.sendJob(job)
    }
}