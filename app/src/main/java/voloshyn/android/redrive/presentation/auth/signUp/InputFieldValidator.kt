package voloshyn.android.redrive.presentation.auth.signUp

interface InputFieldValidator {
    fun <T:SignUpField> validate(fieldType: SignUpFieldsType, update: (T) -> Unit)
}

sealed class SignUpFieldsType {
    class FullName(val fullName: String) : SignUpFieldsType()
    class Email(val email: String) : SignUpFieldsType()
    class Password(val password: String) : SignUpFieldsType()
    class ConfirmPassword(val passwordValue: String, val confirmPasswordValue: String) :
        SignUpFieldsType()
}