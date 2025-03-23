package com.example.domain.useCase

import com.example.domain.model.UserAuthCredentials
import com.example.domain.repository.EmailAuthRepository

class SignUpWithEmailUseCase(private val authRepository: EmailAuthRepository) {
    suspend fun invoke(
        credentials: UserAuthCredentials
    ) {
        authRepository.signUpWithEmail(credentials)
    }


}