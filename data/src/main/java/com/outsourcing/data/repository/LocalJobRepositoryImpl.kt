package com.outsourcing.data.repository

import android.content.res.Resources.NotFoundException
import com.outsourcing.data.dao.LocalJobDao
import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.repository.LocalJobRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalJobRepositoryImpl @Inject constructor(
    private val localJobDao: LocalJobDao
): LocalJobRepository {
    override fun collectLocalJobs(): Flow<List<Job>> {
        return localJobDao.collectLocalJobs()
    }

    override suspend fun getSentJob(): Result<List<Job>> {
        return kotlin.runCatching {
            localJobDao.getSentJob()
        }
    }

    override suspend fun getPendingJob(): Result<List<Job>> {
        return kotlin.runCatching {
            localJobDao.getPendingJob()
        }
    }

    override suspend fun getFailedJob(): Result<List<Job>> {
        return kotlin.runCatching {
            localJobDao.getFailedJob()
        }
    }

    override suspend fun addJob(job: Job): Result<Job> {
        return kotlin.runCatching {
            localJobDao.insertJob(job)
        }
    }

    override suspend fun deleteJob(job: Job): Result<Job> {
        return kotlin.runCatching {
            localJobDao.deleteJob(job) ?: throw NotFoundException()
        }
    }
}