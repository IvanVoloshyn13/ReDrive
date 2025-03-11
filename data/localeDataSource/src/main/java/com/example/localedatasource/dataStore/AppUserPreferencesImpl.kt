package com.example.localedatasource.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppUserPreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppUserPreferences {
    override suspend fun setCurrentUserId(uUid: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_USER] = uUid
        }
    }

    override suspend fun clearCurrentUserId() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.CURRENT_USER)
        }
    }

    override fun observeUserId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.CURRENT_USER]
        }
    }
}