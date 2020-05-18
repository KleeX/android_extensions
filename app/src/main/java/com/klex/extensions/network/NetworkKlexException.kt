package com.klex.extensions.network

import com.klex.extensions.KlexException

class NetworkKlexException(
    val serverMessage: String? = null,
    val errorCode: String? = null
) : KlexException()