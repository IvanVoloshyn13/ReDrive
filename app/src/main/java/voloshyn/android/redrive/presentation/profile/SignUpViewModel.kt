package voloshyn.android.redrive.presentation.profile

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.User
import voloshyn.android.domain.models.UserTuple
import voloshyn.android.domain.useCase.profile.SignUpUseCase
import voloshyn.android.domain.useCase.profile.ValidateEmailUseCase
import voloshyn.android.domain.useCase.profile.ValidateFullNameUseCase
import voloshyn.android.domain.useCase.profile.ValidatePasswordUseCase
import voloshyn.android.redrive.utils.message
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUp: SignUpUseCase,
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
            currentValidationState.collectLatest { valid ->
                _state.update {
                    it.copy(
                        validationState = valid.isValid
                    )
                }
            }
        }
    }

    suspend fun signUp(user: User) {
        _state.update {
            it.copy(
                loading = true
            )
        }
        viewModelScope.launch {
            val result = signUp.invoke(user)
            when (result) {
                is AppResult.Success -> _state.update {
                    it.copy(
                        currentUser = result.data,
                        isSignUp = true,
                        loading = false
                    )
                }

                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            errorMessage = result.error.toStringResource(),
                            isError = true
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


data class ValidationState(
    val isValidFullName: Boolean = false,
    val isValidEmail: Boolean = false,
    val isValidPassword: Boolean = false,
    val isValidConfirmPassword: Boolean = false
) {
    val isValid
        get() = isValidFullName && isValidEmail && isValidPassword && isValidConfirmPassword

}

data class FragmentSignUpState(
    val validationState: Boolean = false,
    val isSignUp: Boolean = false,
    val loading: Boolean = false,
    val currentUser: UserTuple = UserTuple(),
    val isError: Boolean = false,
    @StringRes val errorMessage: Int = 0
)



