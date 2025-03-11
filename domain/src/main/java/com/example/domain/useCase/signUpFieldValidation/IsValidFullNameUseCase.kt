package com.example.domain.useCase.signUpFieldValidation

object IsValidFullNameUseCase {

    operator fun invoke(fullName: String): Boolean {
        val names = fullName.split(" ").filter { it.isNotEmpty() }
        val isValid = names.size > 1
        return isValid
    }

}