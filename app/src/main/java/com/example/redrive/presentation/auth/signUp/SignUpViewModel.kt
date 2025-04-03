package com.example.redrive.presentation.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.AppException
import com.example.domain.model.UserAuthCredentials
import com.example.domain.useCase.SignUpWithEmailUseCase
import com.example.domain.useCase.signUpFieldValidation.IsValidConfirmPasswordUseCase
import com.example.domain.useCase.signUpFieldValidation.IsValidEmailUseCase
import com.example.domain.useCase.signUpFieldValidation.IsValidFullNameUseCase
import com.example.domain.useCase.signUpFieldValidation.IsValidPasswordUseCase
import com.example.domain.useCase.signUpFieldValidation.PasswordValidationResult
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEBOUNCE_TIME_MILLIS = 250L

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpWithEmailUseCase,
    private val appStringResProvider: AppStringResProvider
) : BaseViewModel() {

    private val _state: MutableStateFlow<FragmentSignUpState> =
        MutableStateFlow<FragmentSignUpState>(
            FragmentSignUpState()
        )

    val state = _state.onSubscription {
        validateAndUpdateFields()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(),
        FragmentSignUpState()
    )


    private val _fullNameInput: MutableStateFlow<String> = MutableStateFlow("")
    private val fullNameInput = _fullNameInput.asStateFlow()

    private val _emailInput: MutableStateFlow<String> = MutableStateFlow("")
    private val emailInput = _emailInput.asStateFlow()

    private val _passwordInput: MutableStateFlow<String> = MutableStateFlow("")
    private val passwordInput = _passwordInput.asStateFlow()

    private val _confirmPasswordInput: MutableStateFlow<String> = MutableStateFlow("")
    private val confirmPasswordInput = _confirmPasswordInput.asStateFlow()

    fun onFullNameTextChange(value: String) {
        _fullNameInput.value = value
    }

    fun onEmailTextChange(value: String) {
        _emailInput.value = value
    }

    fun onPasswordTextChange(value: String) {
        _passwordInput.value = value
    }

    fun onConfirmPasswordTextChange(value: String) {
        _confirmPasswordInput.value = value
    }

    @OptIn(FlowPreview::class)
    private fun validateAndUpdateFields() {
        viewModelScope.launch {
            combine(
                _fullNameInput.debounce(DEBOUNCE_TIME_MILLIS),
                _emailInput.debounce(DEBOUNCE_TIME_MILLIS),
                _passwordInput.debounce(DEBOUNCE_TIME_MILLIS),
                _confirmPasswordInput.debounce(DEBOUNCE_TIME_MILLIS)
            ) { fullName, email, password, confirmPassword ->
                ValidationFlowState(
                    IsValidFullNameUseCase(fullName),
                    IsValidEmailUseCase(email),
                    IsValidPasswordUseCase(password),
                    IsValidConfirmPasswordUseCase(password, confirmPassword)
                )
            }.collectLatest { validation ->
                _state.update {
                    it.copy(
                        fullName = fullNameInput.value,
                        isValidFullName = validation.isValidFullName,
                        email = emailInput.value,
                        isValidEmail = validation.isValidEmail,
                        password = passwordInput.value,
                        isValidPassword = validation.passwordValidationRes,
                        confirmPassword = confirmPasswordInput.value,
                        isValidConfirmPassword = validation.isValidConfPassword
                    )
                }
            }
        }
    }

    fun onSignUpBttClick() {
        val userCredentials = UserAuthCredentials(
            fullName = _state.value.fullName,
            email = _state.value.email,
            password = _state.value.password
        )
        viewModelScope.launch {
            _state.update {
                it.copy(loading = true)
            }
            try {
                signUpUseCase.invoke(userCredentials)
                _state.update {
                    it.copy(
                        loading = false
                    )
                }
                navigate(Router.SignUpDirections.ToProfile)
            } catch (e: AppException) {
                _state.update {
                    it.copy(
                        loading = false
                    )
                }
                emitError(appStringResProvider.provideStringRes(e))
            }
        }
    }


    private data class ValidationFlowState(
        val isValidFullName: Boolean,
        val isValidEmail: Boolean,
        val passwordValidationRes: PasswordValidationResult,
        val isValidConfPassword: Boolean
    )

}