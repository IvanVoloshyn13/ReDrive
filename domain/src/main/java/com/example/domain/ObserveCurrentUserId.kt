package com.example.domain

import com.example.domain.repository.UserSessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class ObserveCurrentUserId @Inject constructor(
    val repository: UserSessionRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    inline operator fun <R> invoke(crossinline block: suspend (String) -> Flow<R>): Flow<R> {
        return repository.observeCurrentUserId().distinctUntilChanged().flatMapLatest {
            it?.let {
                block(it)
            } ?: return@flatMapLatest emptyFlow()
        }
    }
}