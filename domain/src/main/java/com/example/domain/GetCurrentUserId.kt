package com.example.domain

import com.example.domain.repository.UserSessionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCurrentUserId @Inject constructor(
    private val repository: UserSessionRepository
) {
    suspend operator fun invoke() = repository.observeCurrentUserId().first()
}