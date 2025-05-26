package com.example.localedatasource.dataStore

interface SyncMetadataRepository {
    suspend fun getVehiclesSyncTimestamp(): Long?
    suspend fun setVehicleSyncTimestamp(timestamp: Long)


    suspend fun getLastRefuelPullTimestamp(): Long?
    suspend fun updateLastRefuelPullTimestamp(timestamp: Long)
}