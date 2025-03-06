package com.example.redrive.presentation.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.appResult.AppResult
import com.example.domain.model.UserAuthCredentials
import com.example.domain.useCase.IsValidConfirmPasswordUseCase
import com.example.domain.useCase.IsValidEmailUseCase
import com.example.domain.useCase.IsValidFullNameUseCase
import com.example.domain.useCase.IsValidPasswordUseCase
import com.example.domain.useCase.SignUpWithEmailUseCase
import com.example.redrive.getStringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEBOUNCE_TIME_MILLIS = 250L

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpWithEmailUseCase
) : ViewModel() {


    private val _state: MutableStateFlow<FragmentSignUpState> =
        MutableStateFlow<FragmentSignUpState>(
            FragmentSignUpState()
        )

    val state = _state.onSubscription {
        validateAndUpdate.forEach {
            it()
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(),
        FragmentSignUpState()
    )

    private val validateAndUpdate by lazy {
        arrayOf(
            ::validateAndUpdateFullName,
            ::validateAndUpdateEmail,
            ::validateAndUpdatePassword,
            ::validateAndUpdateConfirmPassword
        )
    }

    private val fullNameInput: MutableSharedFlow<String> = MutableSharedFlow()
    private val emailInput: MutableSharedFlow<String> = MutableSharedFlow()
    private val passwordInput: MutableSharedFlow<String> = MutableSharedFlow()
    private val confirmPasswordInput: MutableSharedFlow<String> = MutableSharedFlow()

    fun setFullNameInput(value: String) {
        viewModelScope.launch {
            fullNameInput.emit(value)
        }
    }

    fun setEmailInput(value: String) {
        viewModelScope.launch {
            emailInput.emit(value)
        }
    }

    fun setPasswordInput(value: String) {
        viewModelScope.launch {
            passwordInput.emit(value)
        }
    }

    fun setConfirmPasswordInput(value: String) {
        viewModelScope.launch {
            confirmPasswordInput.emit(value)
        }
    }

    @OptIn(FlowPreview::class)
    private fun validateAndUpdateFullName() {
        viewModelScope.launch {
            fullNameInput.debounce(DEBOUNCE_TIME_MILLIS).collectLatest { name ->
                if (name == _state.value.fullName) return@collectLatest
                val isValid = IsValidFullNameUseCase(name)
                _state.update {
                    it.copy(
                        fullName = name,
                        isValidFullName = isValid
                    )
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun validateAndUpdateEmail() {
        viewModelScope.launch {
            emailInput.debounce(DEBOUNCE_TIME_MILLIS).collectLatest { email ->
                if (email == _state.value.email) return@collectLatest
                val isValid = IsValidEmailUseCase(email)
                _state.update {
                    it.copy(
                        email = email,
                        isValidEmail = isValid
                    )
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun validateAndUpdatePassword() {
        viewModelScope.launch {
            passwordInput.debounce(DEBOUNCE_TIME_MILLIS).collectLatest { password ->
                if (password == _state.value.password) return@collectLatest
                val passwordValidationState = IsValidPasswordUseCase(password)
                _state.update {
                    it.copy(
                        password = password,
                        isValidPassword = passwordValidationState
                    )
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun validateAndUpdateConfirmPassword() {
        viewModelScope.launch {
            confirmPasswordInput.debounce(DEBOUNCE_TIME_MILLIS).collectLatest { confPassword ->
                if (confPassword == _state.value.confirmPassword) return@collectLatest
                val isValid = IsValidConfirmPasswordUseCase(
                    password = _state.value.password,
                    confirmPassword = confPassword
                )
                _state.update {
                    it.copy(
                        isValidConfirmPassword = isValid
                    )
                }
            }
        }
    }

    fun signUp() {
        val userCredentials = UserAuthCredentials(
            fullName = _state.value.fullName,
            email = _state.value.email,
            password = _state.value.password
        )
        viewModelScope.launch {
            _state.update {
                it.copy(loading = true)
            }
            val result = signUpUseCase.invoke(userCredentials)
            when (result) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            signUpStatus = SignUpStatus.SignIn,
                            loading = false
                        )
                    }
                }

                is AppResult.Error -> {
                    val errorMessage = result.exception.getStringResource()
                    _state.update {
                        it.copy(
                            loading = false,
                            signUpStatus = SignUpStatus.Failure,
                            signUpErrorMessage = errorMessage
                        )
                    }
                }
            }
        }

    }

    fun resetState() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    signUpStatus = SignUpStatus.SignOut,
                    loading = false
                )
            }
        }
    }


}