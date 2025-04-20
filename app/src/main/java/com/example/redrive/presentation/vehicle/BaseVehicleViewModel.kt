package com.example.redrive.presentation.vehicle

import androidx.lifecycle.viewModelScope
import com.example.domain.model.VehicleType
import com.example.redrive.core.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

abstract class BaseVehicleViewModel : BaseViewModel(), BaseVehicleViewModelContract {
    protected val mVehicleTypeSwitcher = MutableStateFlow<VehicleType>(VehicleType.Car)
    protected val mVehicleNameInput = MutableStateFlow<String>("")
    protected val mOdometerReadingInput = MutableStateFlow<String>("")

    override val vehicleTypeInput: StateFlow<VehicleType>
        get() = mVehicleTypeSwitcher.asStateFlow()
    override val vehicleNameInput: StateFlow<String>
        get() = mVehicleNameInput.asStateFlow()
    override val odometerReadingInput: StateFlow<String>
        get() = mOdometerReadingInput.asStateFlow()
     override val isSaveBtnEnabled: StateFlow<Boolean>
        get() = combine(mVehicleNameInput, mOdometerReadingInput) { n, o ->
            n.isNotEmpty() && o.isNotEmpty()
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    override fun onVehicleTypeSwitcherClick(type: VehicleType) {
        mVehicleTypeSwitcher.value = type
    }

    override fun onVehicleNameTextChange(name: String) {
        mVehicleNameInput.onTextChange(name)
    }

    override fun onVehicleOdometerTextChange(odometerReading: String) {
        mOdometerReadingInput.onTextChange(odometerReading)
    }

}