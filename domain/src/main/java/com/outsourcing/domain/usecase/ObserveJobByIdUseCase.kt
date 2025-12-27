package com.outsourcing.domain.usecase

import com.outsourcing.domain.repository.LocalJobRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveJobByIdUseCase @Inject constructor(
    private val localJobRepository: LocalJobRepository,
) {
    operator fun invoke(id: String) = localJobRepository.observeJobById(id)
}