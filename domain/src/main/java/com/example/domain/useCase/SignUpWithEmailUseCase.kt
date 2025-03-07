package com.example.domain.useCase

import com.example.domain.appResult.AppResult
import com.example.domain.appResult.AuthException
import com.example.domain.model.SignInStatus
import com.example.domain.model.UserAuthCredentials
import com.example.domain.repository.EmailAuthRepository

class SignUpWithEmailUseCase(private val authRepository: EmailAuthRepository) {
    suspend fun invoke(
       credentials: UserAuthCredentials
    ): AppResult<SignInStatus, AuthException> {
        return authRepository.signUpWithEmail(credentials)
    }

}