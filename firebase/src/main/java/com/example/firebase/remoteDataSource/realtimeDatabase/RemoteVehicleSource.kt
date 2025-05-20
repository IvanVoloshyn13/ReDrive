package com.example.firebase.remoteDataSource.realtimeDatabase

import com.example.firebase.remoteDataSource.models.VehicleDto

interface RemoteVehicleSource {

    /**
     * Checks whether there are any vehicle records on the remote that have been updated
     * after the given local pull timestamp.
     *
     * @param since The last known local pull timestamp (in millis)
     * @return True if remote has newer vehicle records; false otherwise
     */
    suspend fun shouldDownload(since: Long, currentUserId: String): Boolean

    suspend fun downloadVehicles(uUid: String, since: Long): List<VehicleDto>

    suspend fun uploadVehicles(vehicles: List<VehicleDto>)
}