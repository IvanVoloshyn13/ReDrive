package com.example.localedatasource.dataStore

import kotlinx.coroutines.flow.Flow

interface SyncMetadataRepository {
    suspend fun updateVehicleFetchStatus(status: Int)
    suspend fun getVehicleFetchStatus(status: Int): Flow<Int?>
}