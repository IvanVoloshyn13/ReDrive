package com.example.domain.useCase

import com.example.domain.appResult.AppResult
import com.example.domain.appResult.AuthException
import com.example.domain.model.SignInStatus
import com.example.domain.repository.EmailAuthRepository

class SignInWithEmailUseCase(private val authRepository: EmailAuthRepository) {
    suspend fun invoke(
        email: String,
        password: String
    ): AppResult<SignInStatus, AuthException> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}