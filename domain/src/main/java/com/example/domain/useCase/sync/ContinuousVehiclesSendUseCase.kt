package com.example.domain.useCase.sync

import com.example.domain.ObserveCurrentUserId
import com.example.domain.sync.WorkScheduler
import com.example.domain.sync.SendDataStatusChecker
import com.example.domain.sync.VehiclesSyncChecker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ContinuousVehiclesSendUseCase @Inject constructor(
    @VehiclesSyncChecker private val statusChecker: SendDataStatusChecker,
    private val workScheduler: WorkScheduler,
    private val observeCurrentUserId: ObserveCurrentUserId
) {
    suspend operator fun invoke() {
        observeCurrentUserId { userId ->
            statusChecker.shouldSend(userId)
                .distinctUntilChanged()
                .filter { it }
                .onEach { workScheduler.enqueueSendVehicles(userId) }
        }.collect()
    }

}

