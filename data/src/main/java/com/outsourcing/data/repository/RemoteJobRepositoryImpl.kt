package com.outsourcing.data.repository

import com.outsourcing.data.dao.RemoteJobDao
import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.repository.JobRepository
import javax.inject.Inject

class RemoteJobRepositoryImpl @Inject constructor(

): JobRepository {
    private val remoteJobDao = RemoteJobDao()

    override suspend fun sendJob(job: Job): Result<Job> {
        return runCatching {
            remoteJobDao.sendJob(job)
        }
    }
}