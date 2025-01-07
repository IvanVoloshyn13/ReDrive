package voloshyn.android.redrive.presentation.auth.signUp

import androidx.annotation.StringRes
import voloshyn.android.app.R
import voloshyn.android.domain.models.auth.SignUpStatus

data class FragmentSignUpState(
    val validationState: Boolean = false,
    val signUpStatus: SignUpStatus = SignUpStatus.SignOut,
    val fullNameInput: FieldInputState = FieldInputState(),
    val emailInput: FieldInputState = FieldInputState(),
    val passwordInput: PasswordInputState = PasswordInputState(),
    val confirmPasswordInput: FieldInputState = FieldInputState(),
    val loading: Boolean = false,
    @StringRes val signUpErrorMessage: Int = R.string.empty_string,

    )

data class FieldInputState(
    val value: String = "",
    val isValid: Boolean? = null,
    @StringRes val validationMessage: Int? = null
) : SignUpField

data class PasswordInputState(
    val password:String="",
    val hasUpperCase: Boolean = false,
    val hasLowerCase: Boolean = false,
    val hasDigit: Boolean = false,
    val hasSpecialChar: Boolean = false,
    val hasValidLength: Boolean = false,
    val isValid: Boolean = false
) : SignUpField

interface SignUpField
