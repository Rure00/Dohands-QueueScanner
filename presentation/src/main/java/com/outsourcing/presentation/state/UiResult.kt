package com.outsourcing.presentation.state

sealed class UiResult {
    data class Fail(val msg: String): UiResult()
    data object Loading: UiResult()
    data object Idle: UiResult()
}