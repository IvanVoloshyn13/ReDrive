package com.example.redrive.presentation.vehicles

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Vehicle
import com.example.domain.useCase.vehicle.ConfirmDeleteVehicleUseCase
import com.example.domain.useCase.vehicle.DeleteVehicleUseCase
import com.example.domain.useCase.vehicle.ObserveVehiclesUseCase
import com.example.domain.useCase.vehicle.SetVehicleAsCurrentUseCase
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.wrapLocaleDataSourceRequests
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehiclesViewModel @Inject constructor(
    private val observeVehiclesUseCase: ObserveVehiclesUseCase,
    private val setVehicleAsCurrentUseCase: SetVehicleAsCurrentUseCase,
    private val deleteVehicleUseCase: DeleteVehicleUseCase,
    private val confirmDeleteVehicleUseCase: ConfirmDeleteVehicleUseCase,
    private val stringResProvider: AppStringResProvider
) : BaseViewModel() {

    private val _state: MutableStateFlow<List<Vehicle>> =
        MutableStateFlow(ArrayList())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            vehicles()
        }
    }

    suspend fun vehicles() {
        val vehiclesFlow = observeVehiclesUseCase.invoke()
        vehiclesFlow.collectLatest { vehicles ->
          _state.value=vehicles
        }
    }

    fun onVehicleItemClick(vehicleId: Long) {
        viewModelScope.launch {
            setVehicleAsCurrentUseCase(vehicleId)
        }
    }

    fun onDeleteBtnClick(vehicleId: Long) {
        wrapLocaleDataSourceRequests(
            appStringResProvider = stringResProvider,
            action = {
                deleteVehicleUseCase.invoke(vehicleId)
            }
        ) { message ->
            emitError(message)
        }
    }

    fun confirmDeleteCurrentVehicle(vehicleId: Long) {
        viewModelScope.launch {
            confirmDeleteVehicleUseCase.invoke(vehicleId)
        }
    }

}