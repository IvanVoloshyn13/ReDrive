package com.example.redrive.presentation.vehicle.vehicles

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.VehicleException
import com.example.domain.useCase.vehicle.DeleteVehicleUseCase
import com.example.domain.useCase.vehicle.ObserveVehiclesUseCase
import com.example.domain.useCase.vehicle.SetVehicleAsCurrentUseCase
import com.example.redrive.AppStringResProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val stringResProvider: AppStringResProvider
) : ViewModel() {


    private val _state: MutableStateFlow<VehiclesFragmentState> =
        MutableStateFlow(VehiclesFragmentState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            vehicles()
        }
    }


    suspend fun vehicles() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        val vehiclesFlow = observeVehiclesUseCase.invoke()
        vehiclesFlow.collectLatest { vehicles ->
            _state.update {
                it.copy(
                    isLoading = false,
                    error = false, errorMessage = "",
                    vehicles = vehicles
                )
            }

        }
    }

    fun setVehicleAsCurrent(vehicleId: Long) {
        viewModelScope.launch {
            setVehicleAsCurrentUseCase(vehicleId)
        }
    }

    fun deleteVehicle(vehicleId: Long) {
        viewModelScope.launch {
            try {
                deleteVehicleUseCase.invoke(vehicleId)
            } catch (e: VehicleException.IsCurrentVehicleException) {
                _state.update {
                    it.copy(
                        error = true,
                        errorMessage = stringResProvider.provideStringRes(e)
                    )
                }
            }
        }
    }


    fun resetError() {
        _state.update {
            it.copy(
                error = false,
                errorMessage = ""
            )
        }
    }


}