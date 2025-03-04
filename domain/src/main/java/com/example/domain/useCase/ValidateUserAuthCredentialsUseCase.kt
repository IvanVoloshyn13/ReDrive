package com.example.domain.useCase

internal const val EMAIL_PATTERN = ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+")

internal val EMAIL_REGEX_PATTERN =
    Regex("""[a-zA-Z0-9+._%\-+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+""")

internal const val PASSWORD_MIN_LENGTH = 8

object ValidateUserAuthCredentialsUseCase {

    operator fun invoke(input: InputType): ValidationResult {
        return when (input) {
            is InputType.ConfirmPassword -> validateConfirmPassword(
                password = input.password,
                confirmPassword = input.confirmPassword
            )

            is InputType.Email -> validateEmail(input.email)
            is InputType.FullName -> validateFullName(input.name)
            is InputType.Password -> validatePassword(input.password)
        }
    }

    private fun validateFullName(name: String): ValidationResult {
        val names = name.split(" ").filter { it.isNotEmpty() }
        val isValid = names.size > 1
        val error = if (isValid) null else FullNameError
        return ValidationResult(
            isValid = isValid,
            error = error
        )
    }

    private fun validateEmail(email: String): ValidationResult {
        val isValid = email.matches(Regex(EMAIL_PATTERN))
        val error = if (isValid) null else EmailError
        return ValidationResult(
            isValid = isValid,
            error = error
        )
    }

    private fun validatePassword(password: String): ValidationResult {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val hasValidLength = password.length >= PASSWORD_MIN_LENGTH
        val isValid = hasUpperCase && hasLowerCase && hasDigit && hasValidLength && hasSpecialChar
        val error = when {
            !hasValidLength -> PasswordError.TooShort
            !hasDigit -> PasswordError.MissingDigit
            !hasUpperCase -> PasswordError.MissingUpperCase
            !hasLowerCase -> PasswordError.MissingLowerCase
            !hasSpecialChar -> PasswordError.MissingSpecialChar
            else -> null
        }
        return ValidationResult(
            isValid = isValid,
            error = error
        )
    }

    private fun validateConfirmPassword(
        confirmPassword: String,
        password: String
    ): ValidationResult {
        val isValid = confirmPassword == password
        val error = if (isValid) null else ConfirmPasswordError
        return ValidationResult(
            isValid = isValid,
            error = error
        )
    }
}

sealed class InputType {
    class FullName(val name: String) : InputType()
    class Email(val email: String) : InputType()
    class Password(val password: String) : InputType()
    class ConfirmPassword(val confirmPassword: String, val password: String) : InputType()
}

data class ValidationResult(
    val isValid: Boolean,
    val error: ValidationError?
)

interface ValidationError

enum class PasswordError : ValidationError {
    TooShort, MissingUpperCase, MissingLowerCase, MissingDigit, MissingSpecialChar
}

object EmailError : ValidationError
object FullNameError : ValidationError
object ConfirmPasswordError : ValidationError