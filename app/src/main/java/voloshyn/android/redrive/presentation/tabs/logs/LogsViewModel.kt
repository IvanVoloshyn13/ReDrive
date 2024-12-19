package voloshyn.android.redrive.presentation.tabs.logs

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.useCase.logs.GetLogsUseCase
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val refuelLogs: GetLogsUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<LogsState> = MutableStateFlow(LogsState())
    val state = _state.asStateFlow()

    private val scope = viewModelScope { throwable ->
        _state.update {
            it.copy(
                currentVehicle = Vehicle.DEFAULT_VEHICLE,
                refuelLogs = emptyList(),
                isError = true,
                message = throwable.toStringResource()
            )
        }
    }

    fun setCurrentVehicle(vehicle: Vehicle) {
        _state.update {
            it.copy(currentVehicle = vehicle)
        }
    }

    init {
        //    refuelLogs()
    }

    fun refuelLogs() {
        val vehicleWithLogs = refuelLogs.invoke()
        scope.launch {
            vehicleWithLogs.collectLatest { vehicleWithRefuelLogs ->
                _state.update {
                    it.copy(
                        currentVehicle = TODO(),
                        //    refuelLogs = formatedLogs(vehicleWithRefuelLogs.refuelLogs)
                    )
                }
            }
        }
    }

//    private fun formatedLogs(logs:List<RefuelLog>):List<RefuelLog>{
//       return logs.map {
//            RefuelLog(
//                id=it.id,
//                vehicleId = it.vehicleId,
//                odometer = stringResourceProvider.fromResToText(it.odometer, R.string.value_km),
//                avgFuelConsumption = stringResourceProvider.avgFuelConsumptionFormatting(it.avgFuelConsumption, R.string.value_liter_per_100_km),
//                travelledDistance = stringResourceProvider.fromResToText(it.travelledDistance, R.string.value_km),
//                fuelVolume = stringResourceProvider.fromResToText(it.fuelVolume, R.string.value_liter),
//                payments = it.payments,
//                unitPrice = stringResourceProvider.fromResToText(it.unitPrice, R.string.value_pln_per_liter),
//                notes = it.notes
//            )
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}