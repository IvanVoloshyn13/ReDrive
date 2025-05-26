package com.example.domain.useCase.sync.vehicle

import com.example.domain.GetCurrentUserId
import com.example.domain.sync.WorkScheduler
import javax.inject.Inject

class VehiclesFetchUseCase @Inject constructor(
    private val workScheduler: WorkScheduler,
    private val currentUserId: GetCurrentUserId
) {
    suspend operator fun invoke() {
        val userId = currentUserId.invoke() ?: return
        workScheduler.enqueueFetchVehicles(userId)
    }
}