package com.example.redrive.presentation.auth.signUp

import androidx.annotation.StringRes
import com.example.domain.model.SignInStatus
import com.example.domain.useCase.PasswordValidationResult
import com.example.redrive.R

data class FragmentSignUpState(
    val loading: Boolean = false,
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isValidFullName: Boolean = false,
    val isValidEmail: Boolean = false,
    val isValidPassword: PasswordValidationResult = PasswordValidationResult(),
    val isValidConfirmPassword: Boolean = false,
    val signUpStatus: SignUpStatus = SignUpStatus.SignOut,
    @StringRes val signUpErrorMessage: Int = R.string.empty_string
) {
    val signUpButtonState: SignUpButtonState
        get() =
            if (isValidFullName && isValidEmail && isValidConfirmPassword && isValidPassword.isValid) SignUpButtonState.Enabled else SignUpButtonState.Disabled
}

enum class SignUpButtonState {
    Enabled, Disabled
}

enum class SignUpStatus {
    Failure,SignOut,SignIn
}