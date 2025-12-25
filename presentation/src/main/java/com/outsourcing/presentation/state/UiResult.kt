package com.outsourcing.presentation.state

sealed class UiResult {
    data object Success: UiResult()
    data class Fail(val msg: String): UiResult()
    data object Loading: UiResult()
    data object Init: UiResult()
}