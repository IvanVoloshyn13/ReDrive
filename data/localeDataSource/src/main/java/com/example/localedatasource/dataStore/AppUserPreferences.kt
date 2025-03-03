package com.example.localedatasource.dataStore

interface AppUserPreferences {
    suspend fun setCurrentUserId(uUid: String)
    suspend fun clearCurrentUserId()
}