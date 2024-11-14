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
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.useCase.auth.SignInStatusUseCase
import voloshyn.android.domain.useCase.onBoard.OnBoardIsFinishedUseCase
import voloshyn.android.domain.useCase.tabs.vehicle.IsVehicleUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoard: OnBoardIsFinishedUseCase,
    private val signInStatus: SignInStatusUseCase,
    private val isVehicle: IsVehicleUseCase
) : ViewModel() {
    private val scope = viewModelScope()
    private var currentUser: User? = null

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

    private fun checkSignInStatus(): Boolean {
        return when (val signInStatus = signInStatus.invoke()) {
            is SignInStatus.SignIn -> {
                currentUser = signInStatus.user
                true
            }

            SignInStatus.SignOut -> false
        }
    }

    private suspend fun isVehicle(): Boolean {
        return if (currentUser != null) {
            isVehicle.invoke(currentUser!!.id)
        } else false
    }

    private suspend fun fromSplashToDestination() {
        val isSignedIn = checkSignInStatus()
        when {
            (navigateToTabsDestination(isSignedIn)) -> return
            (navigateToNewVehicleDestination()) -> return
            (navigateToOnBoardDestination()) -> return
            else -> navigateToSignInDestination(isSignedIn)
        }

    }

    private suspend fun navigateToTabsDestination(isSignedIn: Boolean): Boolean {
        return if (isSignedIn && isVehicle()) {
            _observeDestination.emit(FromSplashToDestination.ToTabs(user = currentUser!!))
            true
        } else {
            false
        }
    }

    private suspend fun navigateToNewVehicleDestination(): Boolean {
        if (currentUser == null) return false

        return if (!isVehicle()) {
            _observeDestination.emit(FromSplashToDestination.ToAddNewVehicle)
            true
        } else false
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


    private suspend fun navigateToSignInDestination(isSignedIn: Boolean) {
        if (!isSignedIn) {
            _observeDestination.emit(FromSplashToDestination.ToSignIn)
        } else Unit
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}

