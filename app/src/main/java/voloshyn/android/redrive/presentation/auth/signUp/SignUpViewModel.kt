package voloshyn.android.redrive.presentation.auth.signUp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.auth.Credentials
import voloshyn.android.domain.useCase.auth.SignUpWithEmailUseCase
import voloshyn.android.domain.useCase.auth.ValidateEmailUseCase
import voloshyn.android.domain.useCase.auth.ValidateFullNameUseCase
import voloshyn.android.domain.useCase.auth.ValidatePasswordUseCase
import voloshyn.android.redrive.utils.message
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUp: SignUpWithEmailUseCase,
    private val emailValidator: ValidateEmailUseCase
) : ViewModel() {
    private val viewModelScope = viewModelScope()
    private val fullNameValidator = ValidateFullNameUseCase()
    private val passwordValidator = ValidatePasswordUseCase()

    private val currentValidationState: MutableStateFlow<ValidationState> = MutableStateFlow(
        ValidationState()
    )

    private val _state: MutableStateFlow<FragmentSignUpState> =
        MutableStateFlow<FragmentSignUpState>(
            FragmentSignUpState()
        )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            currentValidationState.collectLatest { validationState ->
                _state.update {
                    it.copy(
                        validationState = validationState.isValid
                    )
                }
            }
        }
    }

    suspend fun signUp(credentials: Credentials) {
        _state.update {
            it.copy(
                loading = true,
                signUpState = SignUpState.InProgress,
            )
        }
        viewModelScope.launch {
            val result = signUp.invoke(credentials)
            when (result) {
                is AppResult.Success -> _state.update {
                    it.copy(
                        currentUser = result.data,
                        signUpState = SignUpState.Success,
                        loading = false
                    )
                }

                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            signUpState = SignUpState.Failure(errorMessage = result.error.toStringResource()),
                        )
                    }
                }
            }
        }
    }

    fun isValidFullName(fullName: String): ValidateErrorState {
        val isValid = fullNameValidator.isValidFullName(fullName)
        currentValidationState.update {
            it.copy(
                isValidFullName = isValid
            )
        }


        return ValidateErrorState(
            isValid = isValid,
            message = if (!isValid) R.string.please_enter_real_names else R.string.empty_string
        )
    }

    fun isValidEmail(email: String): ValidateErrorState {
        val isValid = emailValidator.invoke(email)
        currentValidationState.update {
            it.copy(
                isValidEmail = isValid
            )
        }

        return ValidateErrorState(
            isValid = isValid,
            message = if (!isValid) R.string.invalid_email else R.string.empty_string
        )
    }

    fun isValidPassword(password: String): ValidateErrorState {
        val validateState = passwordValidator.isValidPassword(password)
        currentValidationState.update {
            it.copy(
                isValidPassword = validateState.isValid
            )
        }
        return ValidateErrorState(
            isValid = validateState.isValid,
            message = if (!validateState.isValid) validateState.message() else R.string.empty_string
        )
    }

    fun isValidConfirmPassword(password: String, confirmPassword: String): ValidateErrorState {
        val isValid = password == confirmPassword
        currentValidationState.update {
            it.copy(
                isValidConfirmPassword = isValid
            )
        }

        return ValidateErrorState(
            isValid = isValid,
            message = if (!isValid) R.string.password_confirmation_error else R.string.empty_string
        )
    }

}






