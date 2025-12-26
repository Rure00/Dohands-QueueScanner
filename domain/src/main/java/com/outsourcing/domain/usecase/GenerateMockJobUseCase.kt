package com.outsourcing.domain.usecase

import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.entities.JobStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

class GenerateMockJobUseCase @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(rawBarcode: String) = withContext(ioDispatcher) {
        delay(Random.nextLong(15) * 100)

        Job(
            id = UUID.randomUUID().toString(),
            barcode = rawBarcode,
            time = LocalDateTime.now(),
            status = JobStatus.PENDING
        )
    }
}