package com.example.domain.useCase

import com.example.domain.model.account.UserAuthCredentials
import com.example.domain.repository.EmailAuthRepository
import javax.inject.Inject

class SignUpWithEmailUseCase @Inject constructor(private val authRepository: EmailAuthRepository) {
    suspend fun invoke(
        credentials: UserAuthCredentials
    ) {
        authRepository.signUpWithEmail(credentials)
    }


}