package voloshyn.android.redrive.presentation.tabs.vehicles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.domain.IsDefaultVehicleException
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.useCase.vehicle.AddVehicleUseCase
import voloshyn.android.domain.useCase.vehicle.DeleteVehicleUseCase
import voloshyn.android.domain.useCase.vehicle.ObserveVehiclesUseCase
import voloshyn.android.domain.useCase.vehicle.SwitchCurrentVehicleUseCase
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class VehiclesViewModel @Inject constructor(
    private val addVehicleUseCase: AddVehicleUseCase,
    private val deleteVehicleUseCase: DeleteVehicleUseCase,
    private val vehicles: ObserveVehiclesUseCase,
    private val toggleVehicle: SwitchCurrentVehicleUseCase
) : ViewModel() {

    private val scope = viewModelScope() { cause ->
        _state.update {
            it.copy(
                isLoading = false,
                error = true,
                errorMessage = cause.toStringResource()
            )
        }
    }

    private val _state: MutableStateFlow<VehiclesFragmentState> =
        MutableStateFlow(VehiclesFragmentState())
    val state = _state.asStateFlow()

    init {
        scope.launch {
            vehicles()
        }
    }


    fun onIntent(intent: VehicleIntent) {
        when (intent) {
            is VehicleIntent.AddVehicle -> {
                scope.launch {
                    addVehicle(intent.vehicle)
                }
            }

            is VehicleIntent.OnVehicleChange -> {
                onCurrentVehicleChange(intent.vehicleId)
            }

            is VehicleIntent.OnDeleteVehicle -> {
                scope.launch {
                    deleteVehicle(intent.vehicleId)
                }

            }
        }
    }

    private fun onCurrentVehicleChange(vehicleId: Long) {
        scope.launch {
            toggleVehicle.invoke(vehicleId)
        }
    }

    private suspend fun vehicles() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        val vehiclesFlow = vehicles.invoke()
        vehiclesFlow.collectLatest { vehicles ->
            _state.update {
                it.copy(
                    isLoading = false,
                    error = false, errorMessage = R.string.empty_string,
                    vehicles = vehicles
                )
            }

        }
    }


    private suspend fun addVehicle(vehicle: Vehicle) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        addVehicleUseCase.invoke(vehicle)

    }

    private suspend fun deleteVehicle(vehicleId: Long) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        try {
            deleteVehicleUseCase.invoke(vehicleId)
        } catch (e: IsDefaultVehicleException) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = true,
                    errorMessage = R.string.is_current_vehicle_error
                )
            }
        }

    }

    fun resetError() {
        _state.update {
            it.copy(
                error = false,
                errorMessage = -1
            )
        }
    }


}