package voloshyn.android.redrive.presentation.tabs.editVehicle

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.VehicleType
import voloshyn.android.domain.useCase.vehicle.UpdateVehicleUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class EditVehicleViewModel @Inject constructor(
    private val updateVehicleUseCase: UpdateVehicleUseCase
) : ViewModel() {
    private val scope = viewModelScope()

    private val _state: MutableStateFlow<EditVehicleState> = MutableStateFlow(EditVehicleState())

    private val _currentVehicle: MutableStateFlow<EditVehicleModel> =
        MutableStateFlow(EditVehicleModel())

    val state = _state.asStateFlow()

    init {
        compareVehicles()
    }

    fun initState(vehicle: Vehicle) {
        _state.value = EditVehicleState(vehicle = EditVehicleModel.toEditVehicle(vehicle))
        _currentVehicle.value = _state.value.vehicle
    }

    fun updateVehicleName(name: String) {
        _state.update {
            it.copy(
                vehicle = it.vehicle.copy(
                    name = name,
                )
            )
        }
    }

    fun updateOdometer(odometer: String) {
        _state.update {
            it.copy(
                vehicle = it.vehicle.copy(
                    initialOdometerValue = odometer,
                )
            )
        }
    }

    fun updateVehicleType(type: VehicleType) {
        _state.update {
            it.copy(
                vehicle = it.vehicle.copy(
                    type = type,
                )
            )
        }
    }


    private fun compareVehicles() {
        scope.launch {
            combine(_state, _currentVehicle) { state, currentVehicle ->
                state.copy(
                    areVehiclesTheSame = state.vehicle == currentVehicle
                )
            }.collect { updatedState ->
                _state.value = updatedState
            }
        }
    }

    fun updateVehicle() {
        scope.launch {
            updateVehicleUseCase.invoke(_state.value.vehicle.toVehicle())
        }
    }

}