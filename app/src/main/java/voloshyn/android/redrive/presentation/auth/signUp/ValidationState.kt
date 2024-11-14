package voloshyn.android.redrive.presentation.auth.signUp

data class ValidationState(
    val isValidFullName: Boolean = false,
    val isValidEmail: Boolean = false,
    val isValidPassword: Boolean = false,
    val isValidConfirmPassword: Boolean = false
) {
    val isValid
        get() = isValidFullName && isValidEmail && isValidPassword && isValidConfirmPassword

}