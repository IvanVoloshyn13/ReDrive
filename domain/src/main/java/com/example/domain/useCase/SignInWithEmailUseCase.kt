package com.example.domain.useCase

import com.example.domain.repository.EmailAuthRepository

class SignInWithEmailUseCase(private val authRepository: EmailAuthRepository) {
    suspend fun invoke(
        email: String,
        password: String
    ) {
        authRepository.signInWithEmailAndPassword(email, password)
    }

}