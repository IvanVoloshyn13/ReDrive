package com.example.domain.useCase.signUpFieldValidation

internal const val EMAIL_PATTERN = ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+")


object IsValidEmailUseCase {

    operator fun invoke(email: String): Boolean {
        val isValid = email.matches(Regex(EMAIL_PATTERN))
        return isValid
    }

}