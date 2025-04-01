package com.example.redrive.presentation.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
) : ViewModel() {

    private var startDestination: String = ""

    private val _startDestination = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val destination = _startDestination.asSharedFlow()

    fun onArgs(destination: String) {
        viewModelScope.launch {
            if (destination != startDestination) {
                startDestination = destination
                _startDestination.emit(destination)
            }
        }
    }
}