package com.example.redrive.presentation.splash

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.model.account.SignInStatus
import com.example.domain.useCase.sync.HasRemoteVehiclesUpdatesUseCase
import com.example.domain.useCase.userSession.IsUserSignedInUseCase
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val connectivityManager: ConnectivityManager,
    private val hasRemoteVehiclesUpdatesUseCase: HasRemoteVehiclesUpdatesUseCase
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            initial()
        }
    }

    private suspend fun initial() {
        isUserSignedInUseCase.invoke().collectLatest {
            when (it) {
                SignInStatus.Failure -> return@collectLatest
                SignInStatus.SignOut -> navigate(Router.SplashDirections.ToProfile)
                SignInStatus.SignedIn -> handleSignedInStatusWithNavigation()
            }
        }
    }

    private suspend fun handleSignedInStatusWithNavigation() {
        if (isNetworkAvailable()) {
            checkVehicleUpdates()
            checkRefuelsUpdates()
            navigate(Router.SplashDirections.ToApp)
        } else {
            navigate(Router.SplashDirections.ToApp)
        }
    }

    private suspend fun isUserSignedIn() {
        isUserSignedInUseCase.invoke().collectLatest {
            when (it) {
                SignInStatus.Failure -> return@collectLatest
                SignInStatus.SignOut -> navigate(Router.SplashDirections.ToProfile)
                SignInStatus.SignedIn -> navigate(Router.SplashDirections.ToApp)
            }
        }
    }

    private suspend fun checkVehicleUpdates() {

    }

    private suspend fun checkRefuelsUpdates() {

    }

    private fun isNetworkAvailable(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}