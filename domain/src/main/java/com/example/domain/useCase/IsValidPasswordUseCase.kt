package com.example.domain.useCase

internal const val PASSWORD_MIN_LENGTH = 8

object IsValidPasswordUseCase {

    operator fun invoke(password: String): PasswordValidationResult {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val hasValidLength = password.length >= PASSWORD_MIN_LENGTH
        val isValid: Boolean =
            hasUpperCase && hasLowerCase && hasDigit && hasValidLength && hasSpecialChar
        val validationResult = PasswordValidationResult(
            hasValidLength = hasValidLength,
            hasDigit = hasDigit,
            hasUpperCase = hasUpperCase,
            hasLowerCase = hasLowerCase,
            hasSpecialChar = hasSpecialChar,
            isValid = isValid
        )
        return validationResult
    }

}

data class PasswordValidationResult(
    val hasValidLength: Boolean = false,
    val hasDigit: Boolean = false,
    val hasUpperCase: Boolean = false,
    val hasLowerCase: Boolean = false,
    val hasSpecialChar: Boolean = false,
    val isValid: Boolean = false
)