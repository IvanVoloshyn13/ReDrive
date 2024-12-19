package voloshyn.android.redrive.presentation.auth.signIn

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.useCase.auth.SignInWithEmailUseCase
import voloshyn.android.domain.useCase.vehicle.IsVehicleUseCase
import voloshyn.android.domain.useCase.user.ObserveCurrentUserUseCase
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signIn: SignInWithEmailUseCase,
    private val isVehicleUseCase: IsVehicleUseCase,
    private val observeUser: ObserveCurrentUserUseCase
) : ViewModel() {
    private val scope = viewModelScope()

    private val _state: MutableStateFlow<FragmentSignInState> =
        MutableStateFlow(FragmentSignInState())
    val state = _state.asStateFlow()

    fun signIn(email: String, password: String) {
        scope.launch {
            _state.update {
                it.copy(
                    loading = true, signInStatus = SignInStatus.SignOut
                )
            }
            val result = signIn.invoke(email, password)
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
                    val isVehicle = isVehicle().await()
                    _state.update {
                        it.copy(
                            loading = false,
                            signInStatus = SignInStatus.SignIn,
                            isVehicle = isVehicle
                        )
                    }
                }
            }
        }
    }

    private fun isVehicle(retry: Boolean = true): Deferred<Boolean> {
        return scope.async {
            val uuid = observeUser.invoke().firstOrNull()?.id
            if (uuid != null) {
                val isVehicle = isVehicleUseCase.invoke(uuid)
                isVehicle
            } else {
                if (retry) {
                    delay(250)
                    isVehicle(retry = false).await()
                } else false
            }
        }


    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

}

