package com.example.redrive.presentation.vehicle

import com.example.domain.model.VehicleType
import kotlinx.coroutines.flow.StateFlow

interface BaseVehicleViewModelContract {

    val vehicleTypeInput: StateFlow<VehicleType>
    val vehicleNameInput: StateFlow<String>
    val odometerReadingInput: StateFlow<String>
    val isSaveBtnEnabled: StateFlow<Boolean>

    fun onVehicleTypeSwitcherClick(type: VehicleType)
    fun onVehicleNameTextChange(name: String)
    fun onVehicleOdometerTextChange(odometerReading: String)
    fun doOnBtnSaveClick()
}