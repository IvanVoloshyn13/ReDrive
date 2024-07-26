package voloshyn.android.domain.useCase.profile

class ValidatePasswordUseCase {

    fun isValidPassword(password: String): ValidatePasswordState {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val hasValidLength = password.length >= PASSWORD_MIN_LENGTH
        return ValidatePasswordState(
            hasUpperCase = hasUpperCase,
            hasLowerCase = hasLowerCase,
            hasDigit = hasDigit,
            hasValidLength = hasValidLength,
            hasSpecialChar = hasSpecialChar,
            isValid = hasUpperCase && hasLowerCase && hasDigit && hasValidLength && hasSpecialChar
        )

    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
    }
}


data class ValidatePasswordState(
    val hasUpperCase: Boolean = false,
    val hasLowerCase: Boolean = false,
    val hasDigit: Boolean = false,
    val hasSpecialChar: Boolean = false,
    val hasValidLength: Boolean = false,
    val isValid: Boolean = false
)
