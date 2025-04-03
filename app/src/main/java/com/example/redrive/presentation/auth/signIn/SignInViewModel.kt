package com.example.redrive.presentation.auth.signIn

import androidx.lifecycle.viewModelScope
import com.example.domain.AppException
import com.example.domain.useCase.SignInWithEmailUseCase
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInWithEmailUseCase,
    private val appStringResProvider: AppStringResProvider
) : BaseViewModel() {

    private val _state = MutableStateFlow<FragmentSignInState>(FragmentSignInState())
    val state = _state.asStateFlow()

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
                        loading = false
                    )
                }
                navigate(Router.SignInDirections.ToProfile)
            } catch (e: AppException) {
                _state.update {
                    it.copy(
                        loading = false,
                    )
                }
                emitError(appStringResProvider.provideStringRes(e))
            }

        }
    }

    fun onEmailTextChange(email: String) {
        _state.update {
            it.copy(email = email)
        }
    }


    fun onPasswordTextChange(password: String) {
        _state.update {
            it.copy(password = password)
        }
    }

}