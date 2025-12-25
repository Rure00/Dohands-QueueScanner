package com.outsourcing.domain.usecase

import com.outsourcing.domain.repository.LocalJobRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllJobsUseCase @Inject constructor(
    private val localJobRepository: LocalJobRepository,
) {
    operator fun invoke() = localJobRepository.collectLocalJobs()
}