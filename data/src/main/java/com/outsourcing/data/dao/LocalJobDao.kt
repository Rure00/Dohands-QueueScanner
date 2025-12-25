package com.outsourcing.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.outsourcing.domain.entities.Job
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalJobDao {
    @Query("SELECT * FROM job")
    fun collectLocalJobs(): Flow<List<Job>>

    @Query("SELECT * FROM job WHERE status = 'SENT'")
    suspend fun getSentJob(): List<Job>

    @Query("SELECT * FROM job WHERE status = 'PENDING'")
    suspend fun getPendingJob(): List<Job>

    @Query("SELECT * FROM job WHERE status = 'FAILED'")
    suspend fun getFailedJob(): List<Job>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertJob(job: Job): Job

    @Delete
    fun deleteJob(job: Job): Job?
}