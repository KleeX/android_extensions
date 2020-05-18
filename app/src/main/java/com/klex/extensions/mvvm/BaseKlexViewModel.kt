package com.klex.extensions.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.klex.extensions.*
import com.klex.extensions.network.CONNECTION_FAILED
import com.klex.extensions.network.ERROR_GENERAL
import com.klex.extensions.network.NetworkKlexException
import org.koin.core.KoinComponent
import java.net.ConnectException
import java.net.UnknownHostException

open class BaseKlexViewModel : ViewModel(), KoinComponent {
    protected var showErrorStringMutableLiveData: MutableLiveData<SingleEvent<String>> =
        MutableLiveData()
    val showErrorStringLiveData: LiveData<SingleEvent<String>> =
        showErrorStringMutableLiveData.toLiveData()

    protected var showErrorStringResMutableLiveData: MutableLiveData<SingleEvent<String>> =
        MutableLiveData()
    val showErrorStringResLiveData: LiveData<SingleEvent<String>> =
        showErrorStringResMutableLiveData.toLiveData()

    protected var goNextScreenMLD: MutableLiveData<SingleEvent<Int>> =
        MutableLiveData()
    val goNextScreenLD: LiveData<SingleEvent<Int>> =
        goNextScreenMLD.toLiveData()

    protected var goBackMLD: MutableLiveData<SingleEvent<Unit>> =
        MutableLiveData()
    val goBackLD: LiveData<SingleEvent<Unit>> =
        goBackMLD.toLiveData()

    protected var hideKeyboardMLD: MutableLiveData<SingleEvent<Unit>> =
        MutableLiveData()
    val hideKeyboardLD: LiveData<SingleEvent<Unit>> =
        hideKeyboardMLD.toLiveData()

    protected fun handleException(it: ResultKlexModel.Error) {
        val exception = it.exception
        when (exception) {
            is NetworkKlexException -> {
                val message = exception.serverMessage
                val code = exception.errorCode
                when {
                    message != null -> {
                        showErrorStringMutableLiveData.postSingleValue(message)
                    }
                    code != null -> {
                        showErrorStringResMutableLiveData.postSingleValue(code)
                    }
                    else -> {
                        showErrorStringResMutableLiveData.postSingleValue(ERROR_GENERAL)
                    }
                }
            }
            is UnknownHostException -> {
                showErrorStringResMutableLiveData.postSingleValue(CONNECTION_FAILED)
            }
            is ConnectException -> {
                showErrorStringResMutableLiveData.postSingleValue(CONNECTION_FAILED)
            }
        }
    }
}