package com.example.localedatasource.dataStore

import kotlinx.coroutines.flow.Flow

interface AppVehiclePreferences {
    suspend fun setCurrentVehicleId(id: Long)
    suspend fun clearCurrentVehicleId()
    fun observeCurrentVehicleId(): Flow<Long?>
}