package com.example.firebase.remoteDataSource.realtimeDatabase.unitPreferences

import com.example.firebase.remoteDataSource.models.UnitsPrefDto
import com.example.firebase.remoteDataSource.realtimeDatabase.WithRemoteSyncStatusChecker
import kotlinx.coroutines.flow.Flow

interface RemoteUnitsPrefSource : WithRemoteSyncStatusChecker {
    override suspend fun shouldFetch(since: Long, key: String): Boolean

    suspend fun fetch(userId: String, since: Long): List<UnitsPrefDto>

    suspend fun send(
        userId: String,
        unitsPrefDto: List<UnitsPrefDto>,
        since: Long
    ): List<UnitsPrefDto>

}