package com.example.domain.useCase.sync

import com.example.domain.repository.UserSessionRepository
import com.example.domain.sync.WorkScheduler
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StartSyncDataWorkUseCase @Inject constructor(
    private val workScheduler: WorkScheduler,
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke() {
        val userId = userSessionRepository.observeCurrentUserId().first() ?: return
        workScheduler.enqueueFetchDataChain(userId)
    }
}