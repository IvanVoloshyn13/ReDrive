package com.example.domain.useCase.userSession

import com.example.domain.repository.UserSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

const val NO_USER_ID = ""

class ObserveCurrentUserIdUseCase(private val repository: UserSessionRepository) {

    // This use case might be removed in the future because if the user ID is needed, it can be
    // retrieved implicitly in the use case where it's required.
    operator fun invoke(): Flow<String> {
        return repository.observeCurrentUserId().map {
            it ?: NO_USER_ID
        }
    }
}