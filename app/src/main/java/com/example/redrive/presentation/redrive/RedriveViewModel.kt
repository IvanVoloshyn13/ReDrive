package com.example.redrive.presentation.redrive

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.model.VehicleWithOverview
import com.example.domain.useCase.overview.ObserveVehicleWithOverviewUseCase
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
    private val observeVehicleWithOverviewUseCase: ObserveVehicleWithOverviewUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<VehicleWithOverview?>(null)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeVehicleWithOverviewUseCase.invoke().debounce(150).collectLatest {
                _state.emit(it)
            }
        }
    }

    fun onBttAddRefuelClick() {
        navigate(Router.ReDriveDirection.ToRefuel)
    }

    fun onVehiclesDropDownClick(){
        navigate(Router.ReDriveDirection.ToVehicles)
    }
}