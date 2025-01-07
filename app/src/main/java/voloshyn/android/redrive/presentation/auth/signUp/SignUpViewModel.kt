package voloshyn.android.redrive.presentation.auth.signUp

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.Password
import voloshyn.android.domain.models.auth.SignUpStatus
import voloshyn.android.domain.models.auth.UserCredentials
import voloshyn.android.domain.useCase.auth.SignUpWithEmailUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

private const val DEBOUNCE_TIME_MILLIS = 250L

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUp: SignUpWithEmailUseCase,
) : ViewModel() {
    private val viewModelScope = viewModelScope()

    @Inject
    lateinit var validator: InputFieldValidatorImpl

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

    fun signUp() {
        if (!areFieldsValid()) return
        viewModelScope.launch {
            with(_state.value) {
                if (fullNameInput.value.isEmpty() || emailInput.value.isEmpty()
                    || passwordInput.password.isEmpty() || confirmPasswordInput.value.isEmpty()
                ) {
                    _state.update {
                        it.copy(
                            signUpStatus = SignUpStatus.Failure,
                            signUpErrorMessage = R.string.required
                        )
                    }
                }
                return@with
            }
            _state.update {
                it.copy(
                    loading = true
                )
            }
            val credentials = UserCredentials(
                fullName = _state.value.fullNameInput.value,
                email = _state.value.emailInput.value,
                password = _state.value.passwordInput.password
            )
            val result = signUp.invoke(credentials)
            when (result) {
                is AppResult.Success -> _state.update {
                    it.copy(
                        signUpStatus = SignUpStatus.SignUp,
                        loading = false
                    )
                }

                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            signUpStatus = SignUpStatus.Failure,
                            signUpErrorMessage = it.signUpErrorMessage
                        )
                    }
                }
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        var fieldsAreValid: Boolean
        with(_state.value) {
            fieldsAreValid =
                fullNameInput.isValid == true && passwordInput.isValid && emailInput.isValid == true && confirmPasswordInput.isValid == true
            if (!fieldsAreValid) {
                _state.update {
                    it.copy(
                        signUpStatus = SignUpStatus.Failure,
                        signUpErrorMessage = R.string.fields_arent_valid
                    )
                }
            } else return true
        }
        return false
    }


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

    private fun validateAndUpdateFullName() {
        viewModelScope.launch {
            fullNameInput.debounce(DEBOUNCE_TIME_MILLIS).collectLatest {
                validator.validate<FieldInputState>(SignUpFieldsType.FullName(it)) { inputState ->
                    _state.update {
                        it.copy(
                            fullNameInput = inputState
                        )
                    }
                }
            }
        }
    }

    private fun validateAndUpdateEmail() {
        viewModelScope.launch {
            emailInput.debounce(DEBOUNCE_TIME_MILLIS).collectLatest {
                validator.validate<FieldInputState>(SignUpFieldsType.Email(it)) { inputState ->
                    _state.update {
                        it.copy(
                            emailInput = inputState
                        )
                    }
                }
            }
        }
    }

    private fun validateAndUpdatePassword() {
        viewModelScope.launch {
            passwordInput.debounce(DEBOUNCE_TIME_MILLIS).collectLatest {
                validator.validate<PasswordInputState>(SignUpFieldsType.Password(it)) { inputState ->
                    _state.update {
                        it.copy(
                            passwordInput = inputState
                        )
                    }
                }
            }
        }
    }

    private fun validateAndUpdateConfirmPassword() {
        viewModelScope.launch {
            confirmPasswordInput.debounce(DEBOUNCE_TIME_MILLIS).collectLatest {
                validator.validate<FieldInputState>(
                    SignUpFieldsType.ConfirmPassword(
                        passwordValue = _state.value.passwordInput.password,
                        confirmPasswordValue = it
                    )
                ) { inputState ->
                    _state.update {
                        it.copy(
                            confirmPasswordInput = inputState
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






