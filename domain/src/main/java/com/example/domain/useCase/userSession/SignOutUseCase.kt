package com.example.domain.useCase.userSession

import com.example.domain.repository.UserSessionRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val userSessionRepository: UserSessionRepository) {

    suspend operator fun invoke() = userSessionRepository.signOut()
}