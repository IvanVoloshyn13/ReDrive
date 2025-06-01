package com.example.redrive.presentation.splash

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.data.worker.WorkSchedulerImpl
import com.example.domain.model.account.SignInStatus
import com.example.domain.useCase.sync.StartSyncDataWorkUseCase
import com.example.domain.useCase.userSession.IsUserSignedInUseCase
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val startSyncDataWorkUseCase: StartSyncDataWorkUseCase,
    private val workManager: WorkManager,
    private val connectivityManager: ConnectivityManager
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            showProgressBar()
            initial()
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
                    startSyncDataWorkUseCase()
                    if (isNetworkAvailable()) {
                        navigate(Router.SplashDirections.ToApp)
                        observeSyncWorkStatus()
                    } else {
                        navigate(Router.SplashDirections.ToApp)
                    }
                }
            }
        }
    }

    private suspend fun observeSyncWorkStatus() {
        workManager.getWorkInfosForUniqueWorkFlow(WorkSchedulerImpl.Companion.UniqueWorkName.DATA_SYNC_WORK)
            .collectLatest { works ->
                val finished = works.all { it.state == WorkInfo.State.SUCCEEDED }
                if (finished) {
                    navigate(Router.SplashDirections.ToApp)
                } else {
                    val failure = works.any { it.state == WorkInfo.State.FAILED }
                    if (failure) {
                        //TODO some better logs
                        Log.e("SPLASH_SYNC_WORK", "Failure")
                        navigate(Router.SplashDirections.ToApp)
                    }
                }
            }
    }

    private suspend fun showProgressBar() {
        _isLoading.emit(true)
    }

    private fun isNetworkAvailable(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}