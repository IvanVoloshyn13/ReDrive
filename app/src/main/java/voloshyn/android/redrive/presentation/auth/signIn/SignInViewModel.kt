package voloshyn.android.redrive.presentation.auth.signIn

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.useCase.sign_in.SignInWithEmailUseCase
import voloshyn.android.domain.useCase.vehicle.IsVehicleUseCase
import voloshyn.android.domain.useCase.user.ObserveCurrentUserUseCase
import voloshyn.android.redrive.utils.NavigationPath
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInWithEmailUseCase,
    private val isVehicleUseCase: IsVehicleUseCase,
    private val observeUser: ObserveCurrentUserUseCase
) : ViewModel() {
    private val scope = viewModelScope()

    private val _state: MutableStateFlow<FragmentSignInState> =
        MutableStateFlow(FragmentSignInState())
    val state = _state.asStateFlow()

    private val _navigation: MutableSharedFlow<NavigationPath> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigation = _navigation.asSharedFlow()

    fun signIn(email: String, password: String) {
        scope.launch {
            _state.update {
                it.copy(
                    loading = true, signInStatus = SignInStatus.SignOut
                )
            }
            val result = signInUseCase.invoke(email, password)
            when (result) {
                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            isError = true,
                            errorMessage = result.error.toStringResource(),
                            signInStatus = SignInStatus.Failure
                        )
                    }
                }

                is AppResult.Success -> {
                    val isVehicle = isVehicleUseCase.invoke()
                    _state.update {
                        it.copy(
                            loading = false,
                            signInStatus = SignInStatus.SignIn,
                            isVehicle = isVehicle
                        )
                    }
                    _navigation.tryEmit(
                        if (isVehicle) Navigation.ToTabs else Navigation.ToNewVehicle
                    )
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    sealed class Navigation : NavigationPath {
        data object ToNewVehicle : Navigation()
        data object ToTabs : Navigation()
    }

}

