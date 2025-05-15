package com.example.domain.repository.sync

interface DataSyncRepository {

    /**
     * Checks if newer refuel records exist remotely compared to the last local pull timestamp.
     * Use this on the splash screen if the user is signed in.
     */
    suspend fun hasRemoteRefuelUpdates(): Boolean

    /**
     * Checks if newer vehicle records exist remotely compared to the last local pull timestamp.
     * Use this on the splash screen if the user is signed in.
     */
    suspend fun hasRemoteVehiclesUpdates(): Boolean

}