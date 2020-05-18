package com.klex.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.observeLiveData(liveData: LiveData<T>, body: (T) -> Unit = {}) {
    liveData.observe(this, Observer { it?.let { t -> body(t) } })
}

fun <T> MutableLiveData<T>.toLiveData() = this as LiveData<T>

private fun <T> LiveData<SingleEvent<T>>.eventObserver(body: (T) -> Unit = {}): Observer<SingleEvent<T>> {
    return Observer { this.value?.getContentIfNotHandled()?.let { t -> body(t) } }
}

fun <T> LifecycleOwner.observeLiveDataEvent(
    liveData: LiveData<SingleEvent<T>>,
    body: (T) -> Unit = {}
) {
    liveData.observe(this, liveData.eventObserver(body))
}

fun <T> MutableLiveData<SingleEvent<T>>.postSingleValue(valueToPost: T) =
    postValue(SingleEvent(valueToPost))