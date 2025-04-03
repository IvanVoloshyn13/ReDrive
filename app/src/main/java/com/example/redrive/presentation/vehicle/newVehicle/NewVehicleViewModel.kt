package com.example.redrive.presentation.vehicle.newVehicle

import androidx.lifecycle.viewModelScope
import com.example.domain.UserException
import com.example.domain.model.Vehicle
import com.example.domain.model.VehicleType
import com.example.domain.useCase.vehicle.AddNewVehicleUseCase
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.RedriveDirection
import com.example.redrive.core.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewVehicleViewModel @Inject constructor(
    private val addNewVehicleUseCase: AddNewVehicleUseCase,
    private val appStringResProvider: AppStringResProvider,
) : BaseViewModel() {
    private val _vehicleType = MutableStateFlow<VehicleType>(VehicleType.Default)
    val vehicleTypeState: StateFlow<VehicleType> = _vehicleType.asStateFlow()

    private val _vehicleName: MutableStateFlow<String> = MutableStateFlow("")
    val vehicleNameState = _vehicleName.asStateFlow()

    private val _odometer: MutableStateFlow<String> = MutableStateFlow("")
    val odometerState = _odometer.asStateFlow()

    private val _isBttSaveEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isBttSaveEnabled = _isBttSaveEnabled.asStateFlow()

    private val _errorState: MutableStateFlow<Pair<Boolean, String>> =
        MutableStateFlow(Pair(false, ""))
    val errorState = _errorState.asStateFlow()


    init {
        viewModelScope.launch {
            combine(_vehicleName, _odometer) { name, odometer ->
                Pair<String, String>(name, odometer)
            }.collectLatest {
                if (it.first.isNotEmpty() && it.second.isNotEmpty()) {
                    _isBttSaveEnabled.emit(true)
                } else _isBttSaveEnabled.emit(false)
            }
        }
    }


    fun onVehicleTypeSwitcherClick(type: VehicleType) {
        _vehicleType.value = type
    }

    fun onVehicleNameTextChange(name: String) {
        _vehicleName.value = name
    }

    fun onVehicleOdometerTextChange(name: String) {
        _odometer.value = name
    }

    fun doOnBtnSaveClick() {
        val vehicle = Vehicle(
            id = 0,
            name = _vehicleName.value,
            initialOdometerValue = _odometer.value.toInt(),
            type = _vehicleType.value
        )
        viewModelScope.launch {
            try {
                addNewVehicleUseCase.invoke(vehicle)
                navigate(Router.NewVehicleDirections.ToVehicles)
            } catch (e: UserException.NoUserDetectedException) {
                _errorState.update {
                    it.copy(
                        first = true,
                        second = appStringResProvider.provideStringRes(e)
                    )
                }
            }
        }

    }

    fun resetErrorState() {
        _errorState.update {
            it.copy(
                first = false, second = ""
            )
        }
    }



}