package com.example.localedatasource.dataStore

interface SyncMetadataRepository {
    suspend fun getLastVehiclePullTimestamp(): Long?
    suspend fun updateLastVehiclePullTimestamp(timestamp: Long)

    suspend fun getLastRefuelPullTimestamp(): Long?
    suspend fun updateLastRefuelPullTimestamp(timestamp: Long)
}