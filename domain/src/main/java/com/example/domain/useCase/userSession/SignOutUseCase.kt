package com.example.domain.useCase.userSession

import com.example.domain.repository.UserSessionRepository

class SignOutUseCase(private val userSessionRepository: UserSessionRepository) {

    suspend operator fun invoke() = userSessionRepository.signOut()
}