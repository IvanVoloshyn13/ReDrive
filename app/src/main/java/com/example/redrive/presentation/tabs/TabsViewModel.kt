package com.example.redrive.presentation.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.sync_old.ShouldUploadVehiclesUseCase
import com.example.domain.useCase.sync_old.UploadVehiclesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    private val shouldUploadVehiclesUseCase: ShouldUploadVehiclesUseCase,
    private val uploadVehiclesUseCase: UploadVehiclesUseCase
) : ViewModel() {

    init {
        observeVehiclesToPush()
    }

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

    private fun observeVehiclesToPush() {
        viewModelScope.launch {
            shouldUploadVehiclesUseCase().collectLatest { has ->
                if (has) {
                    uploadVehiclesUseCase()
                } else {
                    Unit
                }
            }
        }
    }


}