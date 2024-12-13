package voloshyn.android.redrive.presentation.newVehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.VehicleType
import voloshyn.android.domain.useCase.tabs.vehicle.AddVehicleUseCase
import voloshyn.android.domain.useCase.tabs.vehicle.IsVehicleUseCase
import voloshyn.android.domain.useCase.user.GetCurrentUserUseCase
import javax.inject.Inject

@HiltViewModel
class NewVehicleViewModel @Inject constructor(
    private val saveVehicle: AddVehicleUseCase
) : ViewModel() {


    private val _state: MutableStateFlow<NewVehicleState> = MutableStateFlow(NewVehicleState())
    val state: StateFlow<NewVehicleState> get() = _state

    fun switchVehicleType(type: VehicleType) {
        _state.update { currentState ->
            currentState.copy(
                vehicleType = type
            )
        }
    }

    fun updateVehicleName(name: String) {
        _state.update { currentState ->
            currentState.copy(
                vehicleName = name
            )
        }
    }

    fun updateOdometer(value: String) {
        _state.update { currentState ->
            currentState.copy(
                odometer = value
            )
        }
    }

    suspend fun saveNewVehicle() {
        if (_state.value.vehicleName.isEmpty()) return
        val vehicle = Vehicle(
            id = 0,
            name = _state.value.vehicleName,
            currentMileage = _state.value.odometer.toInt(),
            type = _state.value.vehicleType
        )
        saveVehicle.invoke(vehicle)
    }
}