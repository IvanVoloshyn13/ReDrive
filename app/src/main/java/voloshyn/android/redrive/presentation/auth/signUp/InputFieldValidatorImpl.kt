package voloshyn.android.redrive.presentation.auth.signUp

import android.util.Patterns
import voloshyn.android.app.R
import javax.inject.Inject

private const val PASSWORD_MIN_LENGTH = 8

class InputFieldValidatorImpl @Inject constructor(
) : InputFieldValidator {

    @Suppress("UNCHECKED_CAST")
    override fun <T : SignUpField> validate(
        fieldType: SignUpFieldsType,
        update: (T) -> Unit
    ) {
        when (fieldType) {
            is SignUpFieldsType.ConfirmPassword -> validateConfirmPassword(
                password = fieldType.passwordValue,
                confirmPassword = fieldType.confirmPasswordValue,
                update = update as (FieldInputState) -> Unit
            )

            is SignUpFieldsType.Email -> validateEmail(
                fieldType.email,
                update as (FieldInputState) -> Unit
            )

            is SignUpFieldsType.FullName -> validateFullName(
                fieldType.fullName,
                update as (FieldInputState) -> Unit
            )

            is SignUpFieldsType.Password -> validatePassword(
                fieldType.password,
                update as (PasswordInputState) -> Unit
            )
        }
    }

    private fun validateFullName(fullName: String, update: (FieldInputState) -> Unit) {
        val names = fullName.split(" ").filter { it.isNotEmpty() }
        val isValid = names.size > 1
        val state = FieldInputState(
            value = fullName,
            isValid = isValid,
            validationMessage = when {
                isValid || fullName.isEmpty() -> null
                else -> R.string.please_enter_real_names
            }
        )
        update(state)
    }

    private fun validateEmail(email: String, update: (FieldInputState) -> Unit) {
        val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val state = FieldInputState(
            value = email,
            isValid = isValid,
            validationMessage = when {
                isValid || email.isEmpty() -> null
                else -> R.string.invalid_email
            }
        )
        update(state)
    }

    private fun validatePassword(password: String, update: (PasswordInputState) -> Unit) {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val hasValidLength = password.length >= PASSWORD_MIN_LENGTH
        val state = PasswordInputState(
            password = password,
            hasUpperCase = hasUpperCase,
            hasLowerCase = hasLowerCase,
            hasDigit = hasDigit,
            hasValidLength = hasValidLength,
            hasSpecialChar = hasSpecialChar,
            isValid = hasUpperCase && hasLowerCase && hasDigit && hasValidLength && hasSpecialChar
        )
        update(state)
    }

    private fun validateConfirmPassword(
        password: String,
        confirmPassword: String,
        update: (FieldInputState) -> Unit
    ) {
        val isValid = password == confirmPassword
        val state = FieldInputState(
            value = confirmPassword,
            isValid = isValid,
            validationMessage = if (isValid) null else R.string.password_confirmation_error
        )
        update(state)
    }
}