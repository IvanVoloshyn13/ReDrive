package com.example.redrive.presentation.vehicle.editVehicle

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Vehicle
import com.example.domain.model.VehicleType
import com.example.domain.useCase.vehicle.EditVehicleUseCase
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditVehicleViewModel @Inject constructor(
    private val editVehicleUseCase: EditVehicleUseCase
) : BaseViewModel() {

    private val oldVehicle = MutableStateFlow<Vehicle>(Vehicle.NO_VEHICLE)

    private val _editedVehicle = MutableStateFlow<Vehicle>(Vehicle.NO_VEHICLE)
    val editedVehicle = _editedVehicle.asStateFlow()

    private val _areVehiclesTheSame = MutableStateFlow<Boolean>(false)
    val areVehiclesTheSame = _areVehiclesTheSame.asStateFlow()

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

    fun onVehicleNameTextChange(name: String) {
        _editedVehicle.update {
            it.copy(
                name = name,
            )
        }
    }

    fun onOdometerTextChange(odometer: String) {
        _editedVehicle.update {
            it.copy(
                initialOdometerValue = odometer.toInt(),
            )
        }
    }

    fun onVehicleTypeChange(type: VehicleType) {
        _editedVehicle.update {
            it.copy(
                type = type,
            )
        }
    }

    fun onSaveBtnClick() {
        viewModelScope.launch {
            editVehicleUseCase(_editedVehicle.value)
            navigate(Router.EditVehicleDirections.ToVehicles)
        }
    }

}