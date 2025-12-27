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
): ViewModel() {
    private val _uiResult = MutableStateFlow<UiResult>(UiResult.Idle)
    val uiResult = _uiResult.asStateFlow()

    private val _jobs = MutableStateFlow<List<Job>>(listOf())
    val jobs = _jobs.asStateFlow()

    private val _isForcedOffline = MutableStateFlow(false)
    val isForcedOffline = _isForcedOffline.asStateFlow()

    init {
        viewModelScope.launch {
            getAllJobsUseCase().collect {
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
                    val newJob = generateMockJobUseCase(intent.rawBarcode)
                    addJobUseCase(newJob)

                    if (!_isForcedOffline.value) {
                        val (_, result) = sendJobUseCase(job = newJob, tryUntil = RETRY_MAX)
                        handleSendJob(result)
                    }

                    _uiResult.value = UiResult.Idle
                }
            }
            is JobIntent.SendOrAddJob -> {
                viewModelScope.launch {
                    if (_isForcedOffline.value) {
                        addJobUseCase(
                            job = intent.job
                        ).onSuccess {
                            _uiResult.value = UiResult.Idle
                        }.onFailure {
                            _uiResult.value = UiResult.Fail(it.message?: "알 수 없는 이유로 실패하였습니다.")
                        }
                    } else {
                        val (_, result) = sendJobUseCase(job = intent.job, tryUntil = RETRY_MAX)
                        handleSendJob(result)
                    }
                }
            }
            is JobIntent.SendJob -> {
                if (_isForcedOffline.value) {
                    _uiResult.value = UiResult.Fail("Offline 입니다.")
                    return
                }
                viewModelScope.launch {
                    val (_, result) = sendJobUseCase(job = intent.job, tryUntil = RETRY_MAX)
                    handleSendJob(result)
                }
            }
            JobIntent.SendPendingJobs -> {
                _uiResult.value = UiResult.Loading
                viewModelScope.launch {
                    Log.d(TAG, "SendPendingJobs: ${_isForcedOffline.value}")
                    if (_isForcedOffline.value) return@launch
                    sendPendingJobsUseCase(RETRY_MAX).forEach { (_, result) ->
                        handleSendJob(result)
                    }
                    _uiResult.value = UiResult.Idle
                }
            }
        }
    }

    private fun handleSendJob(remoteResult: RemoteResult<Job>) {
        when (remoteResult) {
            is RemoteResult.Success -> {
                _uiResult.value = UiResult.Idle
            }
            is RemoteResult.HttpError -> {
                _uiResult.value = UiResult.Fail(remoteResult.body ?: "알 수 없는 이유로 실패하였습니다.")
            }
            is RemoteResult.Offline -> {
                _uiResult.value = UiResult.Fail("네트워크를 확인해주세요.")
            }
            is RemoteResult.Unknown -> {
                _uiResult.value = UiResult.Fail(remoteResult.t.message ?: "알 수 없는 이유로 실패하였습니다.")
            }
        }
    }

    companion object {
        const val TAG = "JobViewModel"
        const val RETRY_MAX = 3
    }
}