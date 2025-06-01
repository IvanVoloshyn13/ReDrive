package com.example.domain.useCase.sync

import com.example.domain.ObserveCurrentUserId
import com.example.domain.sync.WorkScheduler
import com.example.domain.sync.SendDataStatusChecker
import com.example.domain.sync.UnitPreferencesSyncChecker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ContinuousPrefsSendUseCase @Inject constructor(
    private val workScheduler: WorkScheduler,
    private val currentUserId: ObserveCurrentUserId,
    @UnitPreferencesSyncChecker
    private val sendDataStatusChecker: SendDataStatusChecker
) {
    suspend operator fun invoke() {
        currentUserId { userId ->
            sendDataStatusChecker.shouldSend(userId)
                .filter { it }.onEach {
                    workScheduler.enqueueSendUnitPreferences(userId)
                }
        }.collect()
    }

}