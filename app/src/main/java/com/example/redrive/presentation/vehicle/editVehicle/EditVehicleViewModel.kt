package com.example.redrive.presentation.vehicle.editVehicle

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Vehicle
import com.example.domain.model.VehicleType
import com.example.domain.useCase.vehicle.EditVehicleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditVehicleViewModel @Inject constructor(
    private val editVehicleUseCase: EditVehicleUseCase
) : ViewModel() {

    private val oldVehicle = MutableStateFlow<Vehicle>(Vehicle.NO_VEHICLE)

    private val _editedVehicle = MutableStateFlow<Vehicle>(Vehicle.NO_VEHICLE)
    val editedVehicle = _editedVehicle.asStateFlow()

    private val _areVehiclesTheSame = MutableStateFlow<Boolean>(false)
    val areVehiclesTheSame = _areVehiclesTheSame.asStateFlow()

    private val _navigation = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigate = _navigation.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                oldVehicle,
                _editedVehicle
            ) { old, new ->
                old == new
            }.collectLatest {
                _areVehiclesTheSame.value = it
            }
        }
    }

    fun emitVehicle(vehicle: Vehicle) {
        oldVehicle.value = vehicle
        _editedVehicle.value = vehicle
    }

    fun updateVehicleName(name: String) {
        _editedVehicle.update {
            it.copy(
                name = name,
            )
        }
    }

    fun updateOdometer(odometer: String) {
        _editedVehicle.update {
            it.copy(
                initialOdometerValue = odometer.toInt(),
            )
        }
    }

    fun switchVehicleType(type: VehicleType) {
        _editedVehicle.update {
            it.copy(
                type = type,
            )
        }
    }

    fun editVehicle() {
        viewModelScope.launch {
            editVehicleUseCase(_editedVehicle.value)
            _navigation.emit(true)
        }
    }

}