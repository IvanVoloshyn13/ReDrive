package com.example.domain.useCase

object IsValidConfirmPasswordUseCase {

    operator fun invoke(password: String, confirmPassword: String): Boolean {
        return confirmPassword == password
    }

}