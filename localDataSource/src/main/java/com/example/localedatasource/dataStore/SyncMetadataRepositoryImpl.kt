package com.example.localedatasource.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SyncMetadataRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SyncMetadataRepository {

    override suspend fun updateVehicleFetchStatus(status: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.VEHICLE_FETCH_STATUS] = status
        }
    }

    override suspend fun getVehicleFetchStatus(status: Int): Flow<Int?> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.VEHICLE_FETCH_STATUS] ?: TODO("some exception")
        }
    }

}