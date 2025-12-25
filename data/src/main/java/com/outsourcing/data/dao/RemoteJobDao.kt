package com.outsourcing.data.dao

import com.outsourcing.domain.entities.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

// Mock Server
class RemoteJobDao {
    suspend fun sendJob(job: Job) = withContext(Dispatchers.IO) {
        delay(200)

        when (Random.Default.nextInt(100)) {
            in 0..80 -> job
            else -> throw Exception("Server Error")
        }
    }
}