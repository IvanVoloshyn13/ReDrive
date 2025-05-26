package com.example.localedatasource.dataStore

import kotlinx.coroutines.flow.Flow

interface AppVehiclePreferences {
    suspend fun setCurrentVehicleId(id: String)
    suspend fun clearCurrentVehicleId()
    fun observeCurrentVehicleId(): Flow<String?>
}