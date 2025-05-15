package com.example.domain.useCase.sync

import com.example.domain.repository.sync.DataSyncRepository
import javax.inject.Inject

class HasRemoteVehiclesUpdatesUseCase @Inject constructor(
    private val syncRepository: DataSyncRepository
) {
    suspend operator fun invoke() = syncRepository.hasRemoteVehiclesUpdates()
}