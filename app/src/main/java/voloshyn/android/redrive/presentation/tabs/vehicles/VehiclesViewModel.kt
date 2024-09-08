package voloshyn.android.redrive.presentation.tabs.vehicles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.domain.useCase.tabs.vehicles.AddVehicleUseCase
import voloshyn.android.domain.useCase.tabs.vehicles.GetVehiclesUseCase
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class VehiclesViewModel @Inject constructor(
    private val addVehicleUseCase: AddVehicleUseCase,
    private val vehicles: GetVehiclesUseCase
) : ViewModel() {

    private val scope = viewModelScope()
    private val _state: MutableStateFlow<VehiclesState> = MutableStateFlow(VehiclesState())
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
        }
    }

    private suspend fun vehicles() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        val vehicles = vehicles.invoke()
        vehicles.collectLatest { appResult ->
            when (appResult) {
                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = true, errorMessage = appResult.error.toStringResource(),
                            vehicles = emptyList()
                        )
                    }
                }
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = false, errorMessage = R.string.empty_string,
                            vehicles = appResult.data
                        )
                    }
                }
            }
        }
    }


    private suspend fun addVehicle(vehicle: Vehicle, accountId: String?) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        val appResult = addVehicleUseCase.invoke(vehicle, accountId)
        when (appResult) {
            is AppResult.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = true,
                        errorMessage = appResult.error.toStringResource()
                    )
                }
            }

            is AppResult.Success -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = false, errorMessage = R.string.empty_string
                    )
                }
            }
        }
    }


}