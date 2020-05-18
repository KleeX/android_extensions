package com.klex.extensions

sealed class ResultKlexModel<out T> {
    data class Success<out T>(val result: T) : ResultKlexModel<T>()
    data class Error(val exception: Throwable? = null) : ResultKlexModel<Nothing>()
    data class Progress(val inProgress: Boolean = false) : ResultKlexModel<Nothing>()
}