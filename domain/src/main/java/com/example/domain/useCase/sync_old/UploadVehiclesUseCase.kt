package com.example.domain.useCase.sync_old

import com.example.domain.WorkScheduler
import com.example.domain.repository.UserSessionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UploadVehiclesUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository,
    private val workScheduler: WorkScheduler
) {
    suspend operator fun invoke() {
        val uUid = userSessionRepository.observeCurrentUserId().first()!!
        workScheduler.enqueueUploadVehicles(uUid)
    }
}