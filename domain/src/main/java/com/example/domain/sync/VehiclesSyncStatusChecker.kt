package com.example.domain.sync

import com.example.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

interface VehiclesSyncStatusChecker {
    /**
     * Checks if newer vehicle records exist remotely compared to the last local pull timestamp.
     * Use this on the splash screen if the user is signed in.
     */
    suspend fun shouldDownloadFromRemote(uUid: String): Boolean

    /**
     * Use this to observe the Room database for any vehicles that still need
     * to be pushed to the backend.
     *
     * @return a [Flow] emitting if any vehicle with [SyncStatus.PENDING] exist in local
     * database .
     */
    fun shouldUploadToRemote(userId: String): Flow<Boolean>
}