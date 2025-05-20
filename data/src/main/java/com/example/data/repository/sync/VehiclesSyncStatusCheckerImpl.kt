package com.example.data.repository.sync

import com.example.domain.sync.VehiclesSyncStatusChecker
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteVehicleSource
import com.example.localedatasource.dataStore.SyncMetadataRepository
import com.example.localedatasource.room.daos.VehiclesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VehiclesSyncStatusCheckerImpl @Inject constructor(
    private val syncMetadataRepository: SyncMetadataRepository,
    private val remoteVehicleSource: RemoteVehicleSource,
    private val vehiclesDao: VehiclesDao,
) : VehiclesSyncStatusChecker {
    override suspend fun shouldDownloadFromRemote(uUid: String): Boolean {
        return syncMetadataRepository.getLastRefuelPullTimestamp()?.let {
            remoteVehicleSource.shouldDownload(it, uUid)
        } ?: remoteVehicleSource.shouldDownload(0L, uUid)
    }

    override fun shouldUploadToRemote(userId: String): Flow<Boolean> {
        return vehiclesDao.hasPendingVehicles(userId)
    }
}