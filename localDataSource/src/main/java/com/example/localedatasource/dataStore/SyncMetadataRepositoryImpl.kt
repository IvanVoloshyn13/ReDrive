package com.example.localedatasource.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SyncMetadataRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SyncMetadataRepository {
    override suspend fun getLastVehiclePullTimestamp(): Long? {
        return dataStore.data.map {
            it[PreferencesKeys.LAST_PULL_VEHICLES]
        }.firstOrNull()
    }

    override suspend fun updateLastVehiclePullTimestamp(timestamp: Long) {
        dataStore.edit {
            it[PreferencesKeys.LAST_PULL_VEHICLES] = timestamp
        }
    }

    override suspend fun getLastRefuelPullTimestamp(): Long? {
        return dataStore.data.map {
            it[PreferencesKeys.LAST_PULL_REFUELS]
        }.firstOrNull()
    }

    override suspend fun updateLastRefuelPullTimestamp(timestamp: Long) {
        dataStore.edit {
            it[PreferencesKeys.LAST_PULL_REFUELS] = timestamp
        }
    }
}