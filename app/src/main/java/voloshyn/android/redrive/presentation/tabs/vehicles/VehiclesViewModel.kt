package voloshyn.android.redrive.presentation.tabs.vehicles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.useCase.tabs.vehicle.RememberCurrentVehicleUseCase
import voloshyn.android.domain.useCase.tabs.vehicle.AddVehicleUseCase
import voloshyn.android.domain.useCase.tabs.vehicle.GetVehiclesUseCase
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class VehiclesViewModel @Inject constructor(
    private val addVehicleUseCase: AddVehicleUseCase,
    private val vehicles: GetVehiclesUseCase,
    private val toggleVehicle: RememberCurrentVehicleUseCase
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

    private val _state: MutableStateFlow<VehiclesFragmentState> = MutableStateFlow(VehiclesFragmentState())
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
                    addVehicle(intent.vehicle, intent.accountId)
                }
            }

            is VehicleIntent.OnVehicleChange -> {
                onCurrentVehicleChange(intent.vehicleId)
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


    private suspend fun addVehicle(vehicle: Vehicle, accountId: String?) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
       val vehicleId= addVehicleUseCase.invoke(vehicle, accountId)
       onCurrentVehicleChange(vehicleId)
    }

}