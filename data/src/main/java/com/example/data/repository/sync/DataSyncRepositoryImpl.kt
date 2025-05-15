package com.example.data.repository.sync

import com.example.domain.repository.sync.DataSyncRepository
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteDataStorage
import com.example.localedatasource.dataStore.SyncMetadataRepository
import javax.inject.Inject

class DataSyncRepositoryImpl @Inject constructor(
    private val syncMetadataRepository: SyncMetadataRepository,
    private val remoteDataStorage: RemoteDataStorage
) : DataSyncRepository {
    override suspend fun hasRemoteRefuelUpdates(): Boolean {
        TODO()
    }

    override suspend fun hasRemoteVehiclesUpdates(): Boolean {
        return syncMetadataRepository.getLastRefuelPullTimestamp()?.let {
            remoteDataStorage.hasVehiclesUpdates(it, "someId")
        } ?: remoteDataStorage.hasVehiclesUpdates(0L, "someId")
    }
}