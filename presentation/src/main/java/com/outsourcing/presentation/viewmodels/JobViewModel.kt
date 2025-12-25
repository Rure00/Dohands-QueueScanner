package com.outsourcing.presentation.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.usecase.AddJobUseCase
import com.outsourcing.domain.usecase.DeleteJobUseCase
import com.outsourcing.domain.usecase.GetAllJobsUseCase
import com.outsourcing.domain.usecase.SendFailJobUseCase
import com.outsourcing.domain.usecase.SendJobUseCase
import com.outsourcing.domain.usecase.SendPendingJobsUseCase
import com.outsourcing.domain.usecase.SyncJobConsistentlyUseCase
import com.outsourcing.domain.usecase.SyncJobNowUseCase
import com.outsourcing.presentation.intent.JobIntent
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

    fun emitJobIntent(intent: JobIntent) {
        when(intent) {
            is JobIntent.SendJob -> {
                viewModelScope.launch {
                    if (_isOffline.value) {
                        addJobUseCase.invoke(job = intent.job)
                    } else {
                        sendJobUseCase.invoke(job = intent.job)
                    }
                }
            }
            JobIntent.SendFailJobs -> {
                viewModelScope.launch {
                    if (!_isOffline.value) sendFailJobsUseCase.invoke()
                }
            }
            JobIntent.SendPendingJobs -> {
                viewModelScope.launch {
                    if (!_isOffline.value) sendPendingJobsUseCase.invoke()
                }
            }
        }
    }
}