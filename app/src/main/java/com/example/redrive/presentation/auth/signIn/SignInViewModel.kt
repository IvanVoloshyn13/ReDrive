package com.example.redrive.presentation.auth.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.appResult.AppResult
import com.example.domain.model.SignInStatus
import com.example.domain.useCase.SignInWithEmailUseCase
import com.example.redrive.getStringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInWithEmailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<FragmentSignInState>(FragmentSignInState())
    val state = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigation = _navigation.asSharedFlow()

    fun signIn() {
        viewModelScope.launch {

            _state.update {
                it.copy(
                    loading = true
                )
            }

            val email = _state.value.email
            val password = _state.value.password
            val result = signInUseCase.invoke(email = email, password = password)

            when (result) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            signInStatus = SignInStatus.SignedIn,
                            isError = false,
                            errorMessage = NO_STRING_RES
                        )
                    }
                    _navigation.emit(true)
                }

                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            isError = true,
                            errorMessage = result.exception.getStringResource(),
                            signInStatus = SignInStatus.Failure
                        )
                    }
                    _navigation.emit(false)
                }

            }
        }
    }

    fun updateEmail(email: String) {
        _state.update {
            it.copy(email = email)
        }
    }


    fun updatePassword(password: String) {
        _state.update {
            it.copy(password = password)
        }
    }

    fun resetErrorState() {
        _state.update {
            it.copy(
                loading = false,
                isError = false,
                signInStatus = SignInStatus.SignOut,
                errorMessage = NO_STRING_RES
            )
        }
    }

}