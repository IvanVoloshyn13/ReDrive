package com.example.data.repository.sync

import com.example.domain.sync.SyncStatusChecker
import com.example.firebase.remoteDataSource.realtimeDatabase.vehicles.RemoteVehicleSource
import com.example.localedatasource.room.daos.VehiclesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VehiclesSyncStatusCheckerImpl @Inject constructor(
    private val vehiclesDao: VehiclesDao,
    private val remoteVehicleSource: RemoteVehicleSource
) : SyncStatusChecker {
    override suspend fun shouldFetch(key: String): Boolean {
        val since = vehiclesDao.since(userId = key)
        return remoteVehicleSource.shouldFetch(since, key)
    }

    override fun shouldSend(key: String): Flow<Boolean> {
        return vehiclesDao.hasPendingVehicles(key)
    }
}