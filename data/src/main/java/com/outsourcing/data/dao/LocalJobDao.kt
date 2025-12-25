package com.outsourcing.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.outsourcing.data.entities.Job
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

    @Query("SELECT * FROM job WHERE id = :id")
    suspend fun getJobById(id: String): Job?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJob(job: Job)

    @Delete
    fun deleteJob(job: Job)
}