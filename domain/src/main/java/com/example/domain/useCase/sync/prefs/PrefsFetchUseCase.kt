package com.example.domain.useCase.sync.prefs

import com.example.domain.GetCurrentUserId
import com.example.domain.sync.WorkScheduler
import javax.inject.Inject

class PrefsFetchUseCase @Inject constructor(
    private val workScheduler: WorkScheduler,
    private val currentUserId: GetCurrentUserId
) {
    suspend operator fun invoke() {
        val userId = currentUserId.invoke() ?: return
        workScheduler.enqueueFetchUnitPreferences(userId)
    }


}