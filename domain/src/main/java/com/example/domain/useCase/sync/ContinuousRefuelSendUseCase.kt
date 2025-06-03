package com.example.domain.useCase.sync

import com.example.domain.ObserveCurrentUserId
import com.example.domain.sync.RefuelSendChecker
import com.example.domain.sync.SendDataStatusChecker
import com.example.domain.sync.WorkScheduler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ContinuousRefuelSendUseCase @Inject constructor(
    private val workScheduler: WorkScheduler,
    private val currentUserId: ObserveCurrentUserId,
    @RefuelSendChecker
    private val sendDataStatusChecker: SendDataStatusChecker
) {
    suspend operator fun invoke() {
        currentUserId { userId ->
            sendDataStatusChecker.shouldSend(userId)
                .distinctUntilChanged()
                .filter { it }.onEach {
                    workScheduler.enqueueSendRefuels(userId)
                }
        }.collect()
    }

}