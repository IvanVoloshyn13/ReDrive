package com.example.redrive.presentation.redrive

import androidx.lifecycle.viewModelScope
import com.example.domain.model.VehicleWithStats
import com.example.domain.useCase.stats.ObserveVehicleWithStatsUseCase
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class RedriveViewModel @Inject constructor(
    private val observeVehicleWithStatsUseCase: ObserveVehicleWithStatsUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<VehicleWithStats?>(null)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeVehicleWithStatsUseCase.invoke().debounce(150).collectLatest {
                _state.emit(it)
            }
        }
    }

    fun onBttAddRefuelClick() {
        navigate(Router.ReDriveDirection.ToRefuel)
    }

    fun onVehiclesDropDownClick() {
        navigate(Router.ReDriveDirection.ToVehicles)
    }
}