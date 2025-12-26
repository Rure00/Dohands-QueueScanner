package com.outsourcing.data.repository

import com.outsourcing.data.dao.RemoteJobDao
import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.entities.RemoteResult
import com.outsourcing.domain.repository.JobRepository
import java.io.IOException
import javax.inject.Inject

class RemoteJobRepositoryImpl @Inject constructor(

): JobRepository {
    private val remoteJobDao = RemoteJobDao()

    override suspend fun sendJob(job: Job): RemoteResult<Job> {
        return runCatching {
            val r = remoteJobDao.sendJob(job)

            when {
                (r.isSuccessful) -> RemoteResult.Success(job)
                (r.code() in 400..499) -> {
                    RemoteResult.HttpError(r.code(), r.errorBody()?.string())
                }
                (r.code() in 500..599) -> {
                    RemoteResult.HttpError(r.code(), r.errorBody()?.string())
                }
                else -> RemoteResult.Offline(IOException("Network Not Connected"))
            }
        }.getOrElse { RemoteResult.Unknown(Exception("Not Known")) }
    }
}