package voloshyn.android.redrive.presentation.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.OnBoardStatus
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.useCase.onBoard.OnBoardIsFinishedUseCase
import voloshyn.android.domain.useCase.tabs.vehicle.IsVehicleUseCase
import voloshyn.android.domain.useCase.user.GetCurrentUserUseCase
import voloshyn.android.domain.useCase.user.IsUserSignInUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoard: OnBoardIsFinishedUseCase,
    private val signInStatus: IsUserSignInUseCase,
    private val isVehicle: IsVehicleUseCase,
    private val currentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val scope = viewModelScope()
    private var userUuid: String = ""

    init {
        scope.launch {
            fromSplashToDestination()
        }
    }

    private val _navigation: MutableSharedFlow<NavigationPath> =
        MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_LATEST
        )
    val navigation = _navigation.asSharedFlow()

    private suspend fun fromSplashToDestination() {
        val isSignedIn = checkSignInStatus()
        when {
            (navigateToTabsDestination(isSignedIn)) -> return
            (navigateToNewVehicleDestination()) -> return
            (navigateToOnBoardDestination()) -> return
            else -> navigateToSignInDestination(isSignedIn)
        }
    }

    private  fun checkSignInStatus(): Boolean {
        return when (signInStatus.invoke()) {
            is SignInStatus.SignIn -> {
                userUuid = currentUserUseCase.invoke().id
                true
            }

            SignInStatus.SignOut -> {
                userUuid = ""
                false
            }

            SignInStatus.Failure -> {
                userUuid = ""
                false
            }
        }
    }

    private suspend fun isVehicle(): Boolean {
        return if (userUuid.isNotEmpty()) {
            isVehicle.invoke(userUuid)
        } else false
    }

    private suspend fun navigateToTabsDestination(isSignedIn: Boolean): Boolean {
        return if (isSignedIn && isVehicle()) {
            _navigation.tryEmit(NavigationPath.ToTabs)
            true
        } else {
            false
        }
    }

    private suspend fun navigateToNewVehicleDestination(): Boolean {
        if (userUuid.isEmpty()) return false
        return if (isVehicle()) {
            navigateToTabsDestination(isSignedIn = true)
            false
        } else {
            _navigation.tryEmit(NavigationPath.ToNewVehicle)
            true
        }
    }

    private suspend fun navigateToOnBoardDestination(): Boolean {
        val onBoardStatus = onBoard.invoke()
        return when (onBoardStatus) {
            OnBoardStatus.FINISHED -> false
            OnBoardStatus.IN_PROGRESS -> {
                _navigation.emit(NavigationPath.ToOnBoard)
                true
            }
        }
    }


    private fun navigateToSignInDestination(isSignedIn: Boolean) {
        if (!isSignedIn) {
            _navigation.tryEmit(NavigationPath.ToAuthentication)
        } else Unit
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}

