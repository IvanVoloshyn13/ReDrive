package com.example.redrive.presentation.auth.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.AppException
import com.example.domain.model.SignInStatus
import com.example.domain.useCase.SignInWithEmailUseCase
import com.example.redrive.core.AppStringResProvider
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
    private val signInUseCase: SignInWithEmailUseCase,
    private val appStringResProvider: AppStringResProvider
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

            try {
               signInUseCase.invoke(email = email, password = password)
                _state.update {
                    it.copy(
                        loading = false,
                        signInStatus = SignInStatus.SignedIn,
                        isError = false,
                        errorMessage = ""
                    )
                }
                _navigation.emit(true)
            }catch (e:AppException){
                _state.update {
                    it.copy(
                        loading = false,
                        isError = true,
                        errorMessage = appStringResProvider.provideStringRes(e),
                        signInStatus = SignInStatus.Failure
                    )
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
                errorMessage = ""
            )
        }
    }

}