package voloshyn.android.redrive.presentation.tabs.newRefuel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.tabs.Refuel
import voloshyn.android.domain.useCase.tabs.redrive.GetCurrentVehicleUseCase
import voloshyn.android.domain.useCase.tabs.refuel.AddNewRefuelUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class NewRefuelViewModel @Inject constructor(
    private val addNewRefuel: AddNewRefuelUseCase,
    private val currentVehicle: GetCurrentVehicleUseCase
) : ViewModel() {
    private val scope = viewModelScope()
    private val _state: MutableStateFlow<NewRefuelState> = MutableStateFlow(NewRefuelState())
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

    fun updateState(state: NewRefuelState) {
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

    private fun NewRefuelState.toDomain(): Refuel {
        return Refuel(
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