package com.example.redrive.presentation.splash

import androidx.lifecycle.viewModelScope
import com.example.domain.model.SignInStatus
import com.example.domain.useCase.userSession.IsUserSignedInUseCase
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserSignedInUseCase: IsUserSignedInUseCase
) : BaseViewModel() {
    init {
        isUserSignedIn()
    }

    private fun isUserSignedIn() {
        viewModelScope.launch {
            isUserSignedInUseCase.invoke().collectLatest {
                when (it) {
                    SignInStatus.Failure -> return@collectLatest
                    SignInStatus.SignOut -> navigate(Route.ToProfile)
                    SignInStatus.SignedIn -> navigate(Route.ToRedrive)
                }
            }
        }
    }

    sealed class Route : NavRoute {
        data object ToRedrive : Route()
        data object ToProfile : Route()
    }
}