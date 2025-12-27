package com.outsourcing.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outsourcing.domain.entities.Job
import com.outsourcing.domain.usecase.ObserveJobByIdUseCase
import com.outsourcing.domain.usecase.SendJobUseCase
import com.outsourcing.presentation.state.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobDetailViewModel @Inject constructor(
    private val sendJobUseCase: SendJobUseCase,
    private val observeJobByIdUseCase: ObserveJobByIdUseCase
): ViewModel() {
    private val _uiResult = MutableStateFlow<UiResult>(UiResult.Idle)
    val uiResult = _uiResult.asStateFlow()

    private val _jobFlow = MutableStateFlow<Job?>(null)
    val jobFlow = _jobFlow.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = null
        )


    fun observeJobById(id: String) {
        viewModelScope.launch {
            observeJobByIdUseCase.invoke(id).collectLatest {
                _jobFlow.value = it
            }
        }
    }

    fun sendJob() {
        _uiResult.value = UiResult.Loading
        viewModelScope.launch {
            jobFlow.value?.let {
                sendJobUseCase.invoke(it)
            }
            _uiResult.value = UiResult.Idle
        }
    }
}