package com.outsourcing.domain.usecase

import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.entities.JobStatus
import com.outsourcing.domain.entities.RemoteResult
import com.outsourcing.domain.repository.JobRepository
import com.outsourcing.domain.repository.LocalJobRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendJobUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    private val localJobRepository: LocalJobRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(job: Job, tryUntil: Int) = withContext(ioDispatcher) {
        var updated: Pair<Job, RemoteResult<Job>> = job to RemoteResult.Success(job)
        runCatching {
            for (n in 0 until tryUntil) {
                val remoteResult = jobRepository.sendJob(updated.first)
                updated = handleRemoteResult(updated.first, remoteResult) to remoteResult

                if (updated.first.status == JobStatus.SENT) break
            }
        }.also {
            localJobRepository.updateJob(updated.first)
        }

        updated
    }

    private fun handleRemoteResult(job: Job, remoteResult: RemoteResult<Job>): Job {
        return when (remoteResult) {
            is RemoteResult.Success -> remoteResult.data.copy(status = JobStatus.SENT)
            is RemoteResult.HttpError -> {
                job.copy(
                    retryCount = job.retryCount + 1,
                    status = JobStatus.FAILED,
                    errorText = "Http ${remoteResult.code}: ${remoteResult.body ?: "Unknown Error"}",
                )
            }
            is RemoteResult.Offline -> {
                job.copy(
                    retryCount = job.retryCount + 1,
                    status = JobStatus.FAILED,
                    errorText = "NetworkError: ${remoteResult.e.message}",
                )
            }
            is RemoteResult.Unknown -> {
                job.copy(
                    retryCount = job.retryCount + 1,
                    status = JobStatus.FAILED,
                    errorText = "Unknown: ${remoteResult.t.message}",
                )
            }
        }
    }
}