package com.outsourcing.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.outsourcing.domain.entities.JobStatus
import java.time.LocalDateTime

@Entity(
    tableName = "job"
)
data class Job(
    @PrimaryKey() val id: String,
    val barcode: String,
    val time: String,
    val status: JobStatus,
    val errorText: String? = null,
    val retryCount: Int = 0
)

fun Job.toDomainJob() = com.outsourcing.domain.entities.Job(
    id = this.id,
    barcode = this.barcode,
    time = LocalDateTime.parse(this.time),
    status = this.status,
    errorText = this.errorText,
    retryCount = this.retryCount
)

fun fromDomainJob(job: com.outsourcing.domain.entities.Job): Job {
    return Job(
        id = job.id,
        barcode = job.barcode,
        time = job.time.toString(),
        status = job.status,
        errorText = job.errorText,
        retryCount = job.retryCount
    )
}