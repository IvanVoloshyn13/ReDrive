package com.example.domain.useCase.sync

import com.example.domain.WorkScheduler
import com.example.domain.repository.UserSessionRepository
import com.example.domain.sync.VehiclesSyncStatusChecker
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

/**
 * 1. VehicleId generate using java UUID , so they will be unique across devises
 * 2.From firebase, used ValueEventListener to return flow of data, so then it change, download new data and store in Room
 * and in over way make it from Room to Firebase
 * 3. Fetch for download from Remote and Upload from Local
 * 4. Make Sync with workManager.
 */

class VehiclesSyncUseCase @Inject constructor(
    private val vehiclesSyncStatusChecker: VehiclesSyncStatusChecker,
    private val workScheduler: WorkScheduler,
    private val userSessionRepository: UserSessionRepository
) {

    suspend operator fun invoke() {
        val uUid = userSessionRepository.observeCurrentUserId().firstOrNull() ?: return
        checkRemoteStatus(uUid)
    }

    private suspend fun checkRemoteStatus(uUid: String) {
        if (vehiclesSyncStatusChecker.shouldDownloadFromRemote(uUid)) {
            workScheduler.enqueueDownloadVehicles(uUid)
        }
    }

    private fun checkLocalStatus() {

    }
}