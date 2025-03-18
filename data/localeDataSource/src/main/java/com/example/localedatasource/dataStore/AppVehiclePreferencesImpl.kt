package com.example.localedatasource.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppVehiclePreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppVehiclePreferences {
    override suspend fun setCurrentVehicleId(id: Long) {
        dataStore.edit { pref ->
            pref[PreferencesKeys.CURRENT_VEHICLE] = id
        }
    }

    override suspend fun clearCurrentVehicleId() {
        dataStore.edit { pref ->
            pref.remove(PreferencesKeys.CURRENT_VEHICLE)
        }
    }

    override fun observeCurrentVehicleId(): Flow<Long?> {
        return dataStore.data.map { pref ->
            pref[PreferencesKeys.CURRENT_VEHICLE]
        }
    }
}