package com.outsourcing.domain.usecase

import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.repository.LocalJobRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateJobUseCase @Inject constructor(
    private val localJobRepository: LocalJobRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(job: Job) = withContext(ioDispatcher) {
        localJobRepository.updateJob(job = job)
    }
}