package com.example.domain.useCase

import com.example.domain.model.SignInStatus
import com.example.domain.repository.UserSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IsUserSignedInUseCase(private val repository: UserSessionRepository) {
     operator fun invoke(): Flow<SignInStatus> {
        return repository.observeCurrentUser().map { user ->
            if (user != null) SignInStatus.SignedIn else SignInStatus.SignOut
        }
    }
}