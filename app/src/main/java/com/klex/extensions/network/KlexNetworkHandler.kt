package com.klex.extensions.network

import retrofit2.Response


fun <T, R> Response<R>.handleGeneralResponse(
    converter: ((responseModel: R?) -> T)? = null,
    handleCustomError: ((errorBody: R?) -> NetworkKlexException?)? = null
): T? {
    val body = body()
    if (isSuccessful && body != null) {
        return converter?.invoke(body)
    } else {
        val customException = handleCustomError?.invoke(body)
        throw when {
            customException != null -> customException
            !isSuccessful && code() == 400 -> NetworkKlexException(
                errorCode = CREDENTIALS_INVALID
            )
            !isSuccessful && code() == 401 -> NetworkKlexException(
                errorCode = UNAUTHORIZED
            )
            !isSuccessful && code() == 404 -> NetworkKlexException(
                errorCode = NOT_FOUND
            )
            !isSuccessful && code() == 422 -> NetworkKlexException(
                errorCode = UNAUTHORIZED
            )
            else -> NetworkKlexException(errorCode = ERROR_GENERAL)
        }
    }
}