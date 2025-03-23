package com.example.redrive.presentation.auth.signUp

import com.example.domain.useCase.signUpFieldValidation.PasswordValidationResult

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
    val signUpErrorMessage: String = "",
) {
    val signUpButtonState: SignUpButtonState
        get() =
            if (isValidFullName && isValidEmail && isValidConfirmPassword && isValidPassword.isValid) SignUpButtonState.Enabled else SignUpButtonState.Disabled
}

enum class SignUpButtonState {
    Enabled, Disabled
}

enum class SignUpStatus {
    Failure, SignOut, SignIn
}