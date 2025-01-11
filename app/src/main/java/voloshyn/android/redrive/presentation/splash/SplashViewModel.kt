package voloshyn.android.redrive.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.OnBoardStatus
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.useCase.onBoard.OnBoardIsFinishedUseCase
import voloshyn.android.domain.useCase.vehicle.IsVehicleUseCase
import voloshyn.android.domain.useCase.user.GetCurrentUserUseCase
import voloshyn.android.domain.useCase.sign_in.IsSignedInUseCase
import voloshyn.android.redrive.utils.NavigationPath
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isOnBoardFinishedUseCase: OnBoardIsFinishedUseCase,
    private val isSignedInUseCase: IsSignedInUseCase,
    private val isVehicleUseCase: IsVehicleUseCase,
    private val currentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val scope = viewModelScope()

    init {
        resolveNavigation()
    }

    private val _navigation: MutableSharedFlow<NavigationPath> =
        MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_LATEST
        )
    val navigation = _navigation.asSharedFlow()


    private fun resolveNavigation() {
        scope.launch {
            when {
                (!isOnBoardFinished()) -> return@launch
                (!isSignedIn()) -> return@launch
                (!isVehicle()) -> return@launch
                else -> _navigation.emit(Navigation.ToTabs)
            }
        }
    }

    private suspend fun isOnBoardFinished(): Boolean {
        val onBoardStatus = isOnBoardFinishedUseCase.invoke()
        return when (onBoardStatus) {
            OnBoardStatus.FINISHED -> true
            OnBoardStatus.IN_PROGRESS -> {
                _navigation.emit(Navigation.ToOnBoard)
                false
            }
        }
    }

    private suspend fun isSignedIn(): Boolean {
        return when (isSignedInUseCase.invoke()) {
            is SignInStatus.SignIn -> {
                true
            }

            SignInStatus.SignOut -> {
                _navigation.emit(Navigation.ToAuthentication)
                false
            }

            SignInStatus.Failure -> {
                _navigation.emit(Navigation.ToAuthentication)
                false
            }
        }
    }

    private suspend fun isVehicle(): Boolean {
        val isVehicle = isVehicleUseCase.invoke()
        return if (isVehicle) {
            true
        } else {
            _navigation.emit(Navigation.ToNewVehicle)
            false
        }
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    sealed class Navigation : NavigationPath {
        data object ToOnBoard : Navigation()
        data object ToAuthentication : Navigation()
        data object ToNewVehicle : Navigation()
        data object ToTabs : Navigation()
    }
}

