package com.outsourcing.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.entities.JobStatus
import com.outsourcing.domain.entities.RemoteResult
import com.outsourcing.domain.usecase.AddJobUseCase
import com.outsourcing.domain.usecase.GenerateMockJobUseCase
import com.outsourcing.domain.usecase.GetAllJobsUseCase
import com.outsourcing.domain.usecase.SendFailJobUseCase
import com.outsourcing.domain.usecase.SendJobUseCase
import com.outsourcing.domain.usecase.SendPendingJobsUseCase
import com.outsourcing.domain.usecase.UpdateJobUseCase
import com.outsourcing.presentation.intent.JobIntent
import com.outsourcing.presentation.state.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val generateMockJobUseCase: GenerateMockJobUseCase,
    private val getAllJobsUseCase: GetAllJobsUseCase,
    private val sendJobUseCase: SendJobUseCase,
    private val addJobUseCase: AddJobUseCase,
    private val sendPendingJobsUseCase: SendPendingJobsUseCase,
    private val updateJobUseCase: UpdateJobUseCase
): ViewModel() {
    private val _uiResult = MutableStateFlow<UiResult>(UiResult.Idle)
    val uiResult = _uiResult.asStateFlow()

    private val _jobs = MutableStateFlow<List<Job>>(listOf())
    val jobs = _jobs.asStateFlow()

    private val _isForcedOffline = MutableStateFlow(false)
    val isForcedOffline = _isForcedOffline.asStateFlow()

    init {
        viewModelScope.launch {
            getAllJobsUseCase.invoke().collect {
                _jobs.value = it
            }
        }
    }

    fun setIsOffline(to: Boolean) {
        _isForcedOffline.value = to
    }

    fun emitJobIntent(intent: JobIntent) {
        when(intent) {
            is JobIntent.InquireJob -> {
                _uiResult.value = UiResult.Loading
                viewModelScope.launch {
                    val newJob = generateMockJobUseCase.invoke(intent.rawBarcode)
                    addJobUseCase.invoke(newJob)
                    _uiResult.value = UiResult.Idle
                }
            }
            is JobIntent.SendOrAddJob -> {
                viewModelScope.launch {
                    if (_isForcedOffline.value) {
                        addJobUseCase.invoke(job = intent.job)
                            .onSuccess {
                                _uiResult.value = UiResult.Success
                            }.onFailure {
                                _uiResult.value = UiResult.Fail(it.message?: "알 수 없는 이유로 실패하였습니다.")
                            }
                    } else {
                        handleSendJob(intent.job, sendJobUseCase.invoke(job = intent.job))
                    }
                }
            }
            is JobIntent.SendJob -> {
                if (_isForcedOffline.value) {
                    _uiResult.value = UiResult.Fail("Offline 입니다.")
                    return
                }
                viewModelScope.launch {
                    handleSendJob(intent.job, sendJobUseCase.invoke(job = intent.job))
                }
            }
            JobIntent.SendPendingJobs -> {
                _uiResult.value = UiResult.Loading
                viewModelScope.launch {
                    Log.d(TAG, "SendPendingJobs: ${_isForcedOffline.value}")
                    if (_isForcedOffline.value) return@launch
                    sendPendingJobsUseCase.invoke().forEach { (job, r) ->
                        handleSendJob(job , r)
                    }
                    _uiResult.value = UiResult.Idle
                }
            }
        }
    }

    private suspend fun handleSendJob(job: Job, remoteResult: RemoteResult<Job>) {
        var isSuccess = false
        when (remoteResult) {
            is RemoteResult.Success -> {
                isSuccess = true
                _uiResult.value = UiResult.Idle
            }
            is RemoteResult.HttpError -> {
                val tryFor = maxOf(1, RETRY_MAX - 1 - job.retryCount)
                repeat(tryFor) {
                    delay(1000)
                    sendJobUseCase.invoke(job)
                }
                addJobUseCase.invoke(
                    job.copy(retryCount = job.retryCount + tryFor + 1)
                )
            }
            else -> {
                addJobUseCase.invoke(
                    job.copy(retryCount = job.retryCount + 1)
                )
            }
        }

        Log.d(TAG, "handleSendJob: $isSuccess")

        updateJobUseCase.invoke(
            job.copy(status = if (isSuccess) JobStatus.SENT else JobStatus.FAILED)
        )

        Log.d(TAG, "update Job finished")
    }

    companion object {
        const val TAG = "JobViewModel"
        const val RETRY_MAX = 3
    }
}