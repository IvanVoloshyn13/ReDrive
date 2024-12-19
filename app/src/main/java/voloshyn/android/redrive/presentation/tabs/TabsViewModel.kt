package voloshyn.android.redrive.presentation.tabs

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    private val observeCurrentVehicle: ObserveCurrentVehicleUseCase
) : ViewModel() {
    private val scope = viewModelScope()

    private val _state: MutableStateFlow<Vehicle> = MutableStateFlow(Vehicle.DEFAULT_VEHICLE)
    val currentVehicle: StateFlow<Vehicle> get() = _state
    init {
        scope.launch {
            observeCurrentVehicle.invoke().collectLatest {
                _state.emit(it)
            }
        }
    }

    override fun onCleared() {
        scope.cancel()
        super.onCleared()
    }
}