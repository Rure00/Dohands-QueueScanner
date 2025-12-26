package com.outsourcing.domain.entities

import java.io.IOException

sealed interface RemoteResult<out T> {
    data class Success<T>(val data: T) : RemoteResult<T>
    data class HttpError(val code: Int, val body: String?) : RemoteResult<Nothing>
    data class Offline(val e: IOException) : RemoteResult<Nothing>
    data class Unknown(val t: Throwable) : RemoteResult<Nothing>
}