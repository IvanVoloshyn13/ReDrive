package com.example.data.repository.sync

import com.example.domain.sync.SyncStatusChecker
import com.example.firebase.remoteDataSource.realtimeDatabase.WithRemoteSyncStatusChecker
import com.example.firebase.remoteDataSource.realtimeDatabase.unitPreferences.RemoteUnitsPrefSource
import com.example.localedatasource.dataStore.SyncMetadataRepository
import com.example.localedatasource.room.daos.SettingsDao
import com.example.localedatasource.room.daos.VehiclesDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class UnitsPrefSyncStatusCheckerImpl @Inject constructor(
    private val settingsDao: SettingsDao,
    private val remoteUnitsPrefSource: RemoteUnitsPrefSource
) : SyncStatusChecker {
    override suspend fun shouldFetch(key: String): Boolean {
        val since = settingsDao.since(key).first()
        return remoteUnitsPrefSource.shouldFetch(since = since, key = key)
    }

    override fun shouldSend(key: String): Flow<Boolean> {
        return settingsDao.hasPending(key)
    }
}