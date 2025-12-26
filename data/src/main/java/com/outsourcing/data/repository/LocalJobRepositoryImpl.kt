package com.outsourcing.data.repository

import android.content.res.Resources.NotFoundException
import com.outsourcing.data.dao.LocalJobDao
import com.outsourcing.data.entities.fromDomainJob
import com.outsourcing.data.entities.toDomainJob
import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.repository.LocalJobRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalJobRepositoryImpl @Inject constructor(
    private val localJobDao: LocalJobDao
): LocalJobRepository {
    override fun collectLocalJobs(): Flow<List<Job>> {
        return localJobDao.collectLocalJobs().map {
            it.map { job -> job.toDomainJob() }
        }
    }

    override suspend fun updateJob(job: Job): Result<Job> {
        return runCatching {
            localJobDao.updateJob(fromDomainJob(job))
            job
        }
    }

    override suspend fun getSentJob(): Result<List<Job>> {
        return kotlin.runCatching {
            localJobDao.getSentJob().map { it.toDomainJob() }
        }
    }

    override suspend fun getPendingJob(): Result<List<Job>> {
        return kotlin.runCatching {
            localJobDao.getPendingJob().map { it.toDomainJob() }
        }
    }

    override suspend fun getFailedJob(): Result<List<Job>> {
        return kotlin.runCatching {
            localJobDao.getFailedJob().map { it.toDomainJob() }
        }
    }

    override suspend fun addJob(job: Job): Result<Boolean> {
        return kotlin.runCatching {
            localJobDao.insertJob(fromDomainJob(job))
            true
        }
    }

    override suspend fun deleteJob(job: Job): Result<Boolean> {
        return kotlin.runCatching {
            localJobDao.deleteJob(fromDomainJob(job))
            true
        }
    }
}