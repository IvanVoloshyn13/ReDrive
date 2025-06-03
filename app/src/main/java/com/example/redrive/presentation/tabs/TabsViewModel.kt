package com.example.redrive.presentation.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.sync.ContinuousPrefsSendUseCase
import com.example.domain.useCase.sync.ContinuousRefuelSendUseCase
import com.example.domain.useCase.sync.ContinuousVehiclesSendUseCase
import com.example.redrive.core.NetworkStatus
import com.example.redrive.core.NetworkStatusProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    private val networkStatusProvider: NetworkStatusProvider,
    private val continuousVehiclesSendUseCase: ContinuousVehiclesSendUseCase,
    private val continuousPrefsSendUseCase: ContinuousPrefsSendUseCase,
    private val continuousRefuelSendUseCase: ContinuousRefuelSendUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            observeLocalDataToSend()
        }
    }

    private val _isOnline: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isOnline = _isOnline.asSharedFlow()

    private var startDestination: String = ""

    private val _startDestination = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val destination = _startDestination.asSharedFlow()

    private suspend fun observeLocalDataToSend() {
        networkStatusProvider.networkStatusFlow().collectLatest {
            when (it) {
                NetworkStatus.CONNECTED -> {
                    _isOnline.emit(true)
                    viewModelScope.launch {
                        launch { continuousVehiclesSendUseCase() }
                        launch { continuousPrefsSendUseCase() }
                        launch { continuousRefuelSendUseCase() }
                    }
                }

                NetworkStatus.LOST -> {
                    _isOnline.emit(false)
                }
            }
        }
    }

    fun onArgs(destination: String) {
        viewModelScope.launch {
            if (destination != startDestination) {
                startDestination = destination
                _startDestination.emit(destination)
            }
        }
    }

}