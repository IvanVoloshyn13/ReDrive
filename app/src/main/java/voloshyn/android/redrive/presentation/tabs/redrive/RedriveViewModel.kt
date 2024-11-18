package voloshyn.android.redrive.presentation.tabs.redrive

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.useCase.auth.IsUserSignInUseCase
import voloshyn.android.domain.useCase.tabs.vehicle.GetCurrentVehicleUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class RedriveViewModel @Inject constructor(
    isUserSignInUseCase: IsUserSignInUseCase,
    private val currentVehicleUseCase: GetCurrentVehicleUseCase
) : ViewModel() {

    private val scope = viewModelScope() { throwable ->
        _state.update {
            it.copy(
                isLoading = false,
                currentVehicle = Vehicle.DEFAULT_VEHICLE
            )
        }
    }

    private val _state: MutableStateFlow<RedriveState> = MutableStateFlow(RedriveState())
    val state = _state.asStateFlow()

    init {
        currentUser(isUserSignInUseCase)
        currentVehicle()
    }


    private fun currentUser(isUserSignInUseCase: IsUserSignInUseCase) {
        val currentUser = User.EMPTY_USER
        val result = isUserSignInUseCase.invoke()

        }



    private fun currentVehicle() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            currentVehicleUseCase.invoke().collectLatest { vehicle ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        currentVehicle = vehicle
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

}