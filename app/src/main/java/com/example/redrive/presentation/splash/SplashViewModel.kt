package com.example.redrive.presentation.splash

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.viewModelScope
import com.example.domain.model.account.SignInStatus
import com.example.domain.useCase.sync.vehicle.VehiclesFetchUseCase
import com.example.domain.useCase.sync.prefs.PrefsFetchUseCase
import com.example.domain.useCase.userSession.IsUserSignedInUseCase
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val connectivityManager: ConnectivityManager,
    private val prefsFetchUseCase: PrefsFetchUseCase,
    private val vehicleFetchUseCase: VehiclesFetchUseCase
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            showProgressBar()
            initial()
            hideProgressBar()
        }
    }

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private suspend fun initial() {
        isUserSignedInUseCase.invoke().collectLatest {
            when (it) {
                SignInStatus.Failure -> return@collectLatest
                SignInStatus.SignOut -> navigate(Router.SplashDirections.ToProfile)
                SignInStatus.SignedIn -> {
                        vehicleFetchUseCase()
                        delay(350)
                        prefsFetchUseCase()
                    //navigate function do not wait for the sync completion
                    navigate(Router.SplashDirections.ToApp)
                }
            }
        }
    }


    private suspend fun showProgressBar() {
        _isLoading.emit(true)
    }

    private suspend fun hideProgressBar() {
        _isLoading.emit(false)
    }

    private fun isNetworkAvailable(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}