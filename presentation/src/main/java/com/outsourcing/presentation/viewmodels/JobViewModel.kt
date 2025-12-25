package com.outsourcing.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.usecase.AddJobUseCase
import com.outsourcing.domain.usecase.GetAllJobsUseCase
import com.outsourcing.domain.usecase.SendFailJobUseCase
import com.outsourcing.domain.usecase.SendJobUseCase
import com.outsourcing.domain.usecase.SendPendingJobsUseCase
import com.outsourcing.presentation.intent.JobIntent
import com.outsourcing.presentation.state.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val getAllJobsUseCase: GetAllJobsUseCase,
    private val sendJobUseCase: SendJobUseCase,
    private val addJobUseCase: AddJobUseCase,
    private val sendFailJobsUseCase: SendFailJobUseCase,
    private val sendPendingJobsUseCase: SendPendingJobsUseCase,
): ViewModel() {
    private val _uiResult = MutableStateFlow<UiResult>(UiResult.Init)
    val uiResult = _uiResult.asStateFlow()

    private val _jobs = MutableStateFlow<List<Job>>(listOf())
    val jobs = _jobs.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline = _isOffline.asStateFlow()

    init {
        viewModelScope.launch {
            getAllJobsUseCase.invoke().collect {
                _jobs.value = it
            }
        }
    }

    fun uiResultToInit() {
        _uiResult.value = UiResult.Init
    }

    fun emitJobIntent(intent: JobIntent) {
        when(intent) {
            is JobIntent.SendJob -> {
                viewModelScope.launch {
                    val result = if (_isOffline.value) {
                        addJobUseCase.invoke(job = intent.job)
                    } else {
                        sendJobUseCase.invoke(job = intent.job)
                    }

                    result.onSuccess {
                        _uiResult.value = UiResult.Success
                    }.onFailure {
                        Log.i("JobViewModel", "SendJob Fail: ${it.message}")
                        _uiResult.value = UiResult.Fail(it.message?: "알 수 없는 이유로 실패하였습니다.")
                    }
                }
            }
            JobIntent.SendFailJobs -> {
                viewModelScope.launch {
                    if (_isOffline.value) return@launch

                    sendFailJobsUseCase.invoke()
                }
            }
            JobIntent.SendPendingJobs -> {
                viewModelScope.launch {
                    if (_isOffline.value) return@launch

                    sendPendingJobsUseCase.invoke()
                }
            }
        }
    }
}