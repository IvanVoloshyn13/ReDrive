package com.example.domain.useCase

import com.example.domain.repository.EmailAuthRepository
import javax.inject.Inject

class SignInWithEmailUseCase  @Inject constructor(private val authRepository: EmailAuthRepository) {
    suspend fun invoke(
        email: String,
        password: String
    ) {
        authRepository.signInWithEmailAndPassword(email, password)
    }

}