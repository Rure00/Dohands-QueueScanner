package com.outsourcing.data.dao

import com.outsourcing.domain.entities.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.ResponseBody
import kotlin.random.Random
import retrofit2.Response


// Mock Server
class RemoteJobDao {
    suspend fun sendJob(job: Job): Response<Job> = withContext(Dispatchers.IO) {
        delay(200)

        val n = Random.Default.nextInt(100)
        when {
            n < 60 -> Response.success(job) // 2xx
            n < 80 -> Response.error(
                400,
                ResponseBody
                    .create(
                        MediaType.parse("application/json"),
                        """{"message":"Bad Request"}"""
                    )
            )
            n < 95 -> Response.error(
                500,
                ResponseBody
                    .create(
                        MediaType.parse("application/json"),
                        """{"message":"Server Error"}"""
                    )
            )
            else -> throw java.io.IOException("Network Error")
        }
    }
}