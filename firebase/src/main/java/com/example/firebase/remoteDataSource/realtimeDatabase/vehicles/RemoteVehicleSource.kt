package com.example.firebase.remoteDataSource.realtimeDatabase.vehicles

import com.example.firebase.remoteDataSource.models.VehicleDto
import com.example.firebase.remoteDataSource.realtimeDatabase.WithRemoteSyncStatusChecker

interface RemoteVehicleSource : WithRemoteSyncStatusChecker {
    override suspend fun shouldFetch(since: Long, key: String): Boolean
    suspend fun fetchVehicles(userId: String, since: Long): List<VehicleDto>
    suspend fun sendVehicles(
        vehicles: List<VehicleDto>,
        userId: String,
        since: Long
    ): List<VehicleDto>
}