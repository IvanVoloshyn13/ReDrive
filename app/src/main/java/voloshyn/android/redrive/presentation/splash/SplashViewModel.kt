package voloshyn.android.redrive.presentation.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.OnBoardStatus
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.useCase.auth.SignInStatusUseCase
import voloshyn.android.domain.useCase.onBoard.OnBoardIsFinishedUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoard: OnBoardIsFinishedUseCase,
    private val signInStatus: SignInStatusUseCase,
    //  private val vehicles
) : ViewModel() {
    private val scope = viewModelScope()

    init {
        scope.launch {
            fromSplashToDestination()
        }
    }

    private val _observeDestination: MutableSharedFlow<FromSplashToDestination> =
        MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_LATEST
        )
     val destination = _observeDestination.asSharedFlow()

    private suspend fun fromSplashToDestination() {
        if (navigateToOnBoardDestination()) return
        if (navigateToSignInDestination()) return
        if (navigateToNewVehicleDestination()) return
    }

    private suspend fun navigateToOnBoardDestination(): Boolean {
        val onBoardStatus = onBoard.invoke()
        return when (onBoardStatus) {
            OnBoardStatus.FINISHED -> false
            OnBoardStatus.IN_PROGRESS -> {
                _observeDestination.emit(FromSplashToDestination.ToOnBoard)
                true
            }
        }
    }

    private suspend fun navigateToSignInDestination(): Boolean {
        val signInStatus = signInStatus.invoke()
        return when (signInStatus) {
            SignInStatus.SignIn -> false
            SignInStatus.SignOut -> {
                _observeDestination.emit(FromSplashToDestination.ToSignIn)
                true
            }
        }
    }

    private suspend fun navigateToNewVehicleDestination(): Boolean {
        TODO()
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}

