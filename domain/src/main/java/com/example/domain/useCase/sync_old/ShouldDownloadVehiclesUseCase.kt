package com.example.domain.useCase.sync_old

import com.example.domain.repository.UserSessionRepository
import com.example.domain.sync.VehiclesSyncStatusChecker
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ShouldDownloadVehiclesUseCase @Inject constructor(
    private val statusChecker: VehiclesSyncStatusChecker,
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(): Boolean {
        val id = userSessionRepository.observeCurrentUserId().first() ?: return false
        return statusChecker.shouldDownloadFromRemote(id)
    }

}