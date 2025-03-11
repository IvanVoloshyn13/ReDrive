package com.example.domain.useCase.userSession

import com.example.domain.repository.UserSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val REDRIVE_APP_INITIALS = "RD"

class GetUserInitialsUseCase(private val repository: UserSessionRepository) {
    operator fun invoke(): Flow<String> {
        return repository.observeCurrentUser().map {
            return@map if (it != null) {
                val names = it.fullName.split(" ")
                val firstNameInitial = names[0].first().uppercaseChar().toString()
                val secondNameInitial = names[1].first().uppercaseChar().toString()
                firstNameInitial + secondNameInitial
            } else REDRIVE_APP_INITIALS
        }
    }
}