package voloshyn.android.redrive.presentation.tabs.refuel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.refuel.Refuel
import voloshyn.android.domain.useCase.tabs.vehicle.GetCurrentVehicleUseCase
import voloshyn.android.domain.useCase.tabs.refuel.AddNewRefuelUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class RefuelViewModel @Inject constructor(
    private val addNewRefuel: AddNewRefuelUseCase,
    private val currentVehicle: GetCurrentVehicleUseCase
) : ViewModel() {
    private val scope = viewModelScope()
    private val _state: MutableStateFlow<RefuelState> = MutableStateFlow(RefuelState())
    val state = _state.asStateFlow()

    init {
        scope.launch {
            val vehicle = currentVehicle.invoke()
            vehicle.collectLatest { vehicleTuple ->
                _state.update {
                    it.copy(
                        currentVehicleId = vehicleTuple.id
                    )
                }
            }
        }
    }

    fun updateState(state: RefuelState) {
        _state.update {
            it.copy(
                date = state.date,
                odometer = state.odometer,
                fuelVolume = state.fuelVolume,
                unitPrice = state.unitPrice,
                notes = state.notes,
                fullTank = state.fullTank,
                missedPrevious = state.missedPrevious
            )
        }
    }

    fun addNewRefuel() {
        scope.launch {
            val newRefuel = _state.value.toDomain()
            addNewRefuel.invoke(newRefuel)
        }
    }

    private fun RefuelState.toDomain(): Refuel {
        return Refuel(
            id=0,
            vehicleId = currentVehicleId,
            date = date,
            fuelVolume = fuelVolume.toDouble(),
            odometer = odometer.toInt(),
            unitPrice = unitPrice.toDouble(),
            fullTank = fullTank,
            missedPrevious = missedPrevious,
            notes = notes
        )
    }
}