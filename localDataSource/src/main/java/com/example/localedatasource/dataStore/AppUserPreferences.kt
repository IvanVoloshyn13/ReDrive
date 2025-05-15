package com.example.localedatasource.dataStore

import kotlinx.coroutines.flow.Flow

interface AppUserPreferences {
    suspend fun setCurrentUserId(uUid: String)
    suspend fun clearCurrentUserId()
    fun observeUserId(): Flow<String?>
}