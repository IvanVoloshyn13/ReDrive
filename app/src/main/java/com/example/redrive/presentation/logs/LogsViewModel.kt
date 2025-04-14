package com.example.redrive.presentation.logs

import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.ObserveRefuelLogsUseCase
import com.example.domain.model.VehicleWithLogs
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val observeRefuelLogsUseCase: ObserveRefuelLogsUseCase,
) : BaseViewModel() {

    private val _state: MutableStateFlow<VehicleWithLogs> = MutableStateFlow(VehicleWithLogs())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeRefuelLogsUseCase().collectLatest {
                _state.value = it
            }
        }
    }

    fun onLogItemClick(itemId: Long) {
        navigate(Router.LogsDirections.ToEditRefuel(itemId))
    }
}