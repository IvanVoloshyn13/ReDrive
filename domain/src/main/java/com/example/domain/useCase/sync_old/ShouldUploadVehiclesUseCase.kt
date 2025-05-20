package com.example.domain.useCase.sync_old

import com.example.domain.repository.UserSessionRepository
import com.example.domain.sync.VehiclesSyncStatusChecker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ShouldUploadVehiclesUseCase @Inject constructor(
    private val checker: VehiclesSyncStatusChecker,
    private val userSessionRepository: UserSessionRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Boolean> {
        return userSessionRepository.observeCurrentUserId().flatMapLatest {
            it?.let { uUid ->
                checker.shouldUploadToRemote(uUid)
            } ?: flowOf(false)
        }
    }

}
