package com.example.redrive.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _navigation: MutableSharedFlow<NavRoute?> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigation = _navigation.asSharedFlow()

    fun navigate(route: NavRoute) {
        viewModelScope.launch {
            _navigation.emit(route)
        }
    }
}

interface NavRoute