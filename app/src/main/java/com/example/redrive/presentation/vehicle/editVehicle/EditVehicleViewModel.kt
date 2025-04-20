package com.example.redrive.presentation.vehicle.editVehicle

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Vehicle
import com.example.domain.useCase.vehicle.EditVehicleUseCase
import com.example.redrive.core.Router
import com.example.redrive.presentation.vehicle.BaseVehicleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditVehicleViewModel @Inject constructor(
    private val editVehicleUseCase: EditVehicleUseCase
) : BaseVehicleViewModel() {

    private val oldVehicle = MutableStateFlow<Vehicle>(Vehicle.NO_VEHICLE)

    private val editedVehicle = combine(
        mVehicleNameInput, mOdometerReadingInput, mVehicleTypeSwitcher
    ) { name, odometer, type ->
        Vehicle(
            id = oldVehicle.value.id,
            name = name,
            initialOdometerValue = odometer.toIntOrNull() ?: 0,
            type = type
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, Vehicle.NO_VEHICLE)

    override val isSaveBtnEnabled: StateFlow<Boolean>
        get() = combine(
            oldVehicle,
            editedVehicle
        ) { old, new ->
            old != new
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)


    fun emitVehicle(vehicle: Vehicle) {
        oldVehicle.value = vehicle
        mVehicleNameInput.value = vehicle.name
        mVehicleTypeSwitcher.value = vehicle.type
        mOdometerReadingInput.value = vehicle.initialOdometerValue.toString()
    }

    override fun doOnBtnSaveClick() {
        viewModelScope.launch {
            editVehicleUseCase(editedVehicle.value)
            navigate(Router.EditVehicleDirections.ToVehicles)
        }
    }

}