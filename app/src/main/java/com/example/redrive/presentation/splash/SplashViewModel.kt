package com.example.redrive.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.SignInStatus
import com.example.domain.useCase.userSession.IsUserSignedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserSignedInUseCase: IsUserSignedInUseCase
) : ViewModel() {
    init {
        isUserSignedIn()
    }

    private val _navigation: MutableSharedFlow<Boolean> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigation = _navigation.asSharedFlow()

    private fun isUserSignedIn() {
        viewModelScope.launch {
            isUserSignedInUseCase.invoke().collectLatest {
                when (it) {
                    SignInStatus.Failure -> TODO()
                    SignInStatus.SignOut -> _navigation.emit(false)
                    SignInStatus.SignedIn -> _navigation.emit(true)
                }
            }
        }

    }

}