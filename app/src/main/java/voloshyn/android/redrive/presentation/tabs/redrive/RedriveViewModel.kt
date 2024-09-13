package voloshyn.android.redrive.presentation.tabs.redrive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.tabs.profile.UserTuple
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.domain.models.tabs.redrive.VehicleTuple
import voloshyn.android.domain.useCase.auth.IsSignedInUseCase
import voloshyn.android.domain.useCase.tabs.redrive.GetCurrentVehicleUseCase
import voloshyn.android.domain.useCase.tabs.redrive.StoreCurrentVehicleUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class RedriveViewModel @Inject constructor(
    isSignedInUseCase: IsSignedInUseCase,
    private val currentVehicleUseCase: GetCurrentVehicleUseCase,
    private val storeCurrentVehicleUseCase: StoreCurrentVehicleUseCase
) : ViewModel() {

    private val scope= viewModelScope()

    private val _state: MutableStateFlow<RedriveState> = MutableStateFlow(RedriveState())
    val state = _state.asStateFlow()

    init {
        currentUser(isSignedInUseCase)
        currentVehicle()
    }


    private fun currentUser(isSignedInUseCase: IsSignedInUseCase) {
        val currentUser = UserTuple.EMPTY_USER
        val result = isSignedInUseCase.invoke()
        when (result) {
            is AppResult.Success -> {
                result.data?.let {
                    currentUser.copy(
                        fullName = it.fullName,
                        email = it.email
                    )
                }
            }

            is AppResult.Error -> {}
        }
    }

    fun onCurrentVehicleChange(vehicle: Vehicle) {
        scope.launch {
            storeCurrentVehicleUseCase.invoke(
                VehicleTuple(
                    id = vehicle.id,
                    name = vehicle.name
                )
            )
        }

    }

    fun currentVehicle() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            currentVehicleUseCase.invoke().collectLatest { appResult ->
                when (appResult) {
                    is AppResult.Error -> {}
                    is AppResult.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                currentVehicle = appResult.data
                            )
                        }
                    }
                }
            }
        }
    }


}