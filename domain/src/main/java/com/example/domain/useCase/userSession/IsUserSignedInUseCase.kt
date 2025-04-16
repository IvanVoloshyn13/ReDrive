package com.example.domain.useCase.userSession

import com.example.domain.model.account.SignInStatus
import com.example.domain.repository.UserSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsUserSignedInUseCase  @Inject constructor (private val repository: UserSessionRepository) {
     operator fun invoke(): Flow<SignInStatus> {
        return repository.observeAuthState().map { user ->
            if (user != null) SignInStatus.SignedIn else SignInStatus.SignOut
        }
    }
}