package com.example.redrive.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val _navigation: MutableSharedFlow<AppDirection?> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _error: MutableStateFlow<Pair<Boolean, String>> = MutableStateFlow(Pair(false, ""))
    val error = _error.asStateFlow()

    fun navigate(route: AppDirection) {
        viewModelScope.launch {
            _navigation.emit(route)
        }
    }

    fun emitError(message: String) {
        _error.update {
            it.copy(
                first = true,
                second = message
            )
        }
    }

    fun onErrorShown() {
        viewModelScope.launch {
            _error.update {
                it.copy(
                    first = false,
                    second = ""
                )
            }
        }
    }


    fun <T> MutableStateFlow<T>.onTextChange(value: T) {
        if (this.value != value)
            viewModelScope.launch {
                this@onTextChange.emit(value)
            }
    }

}

