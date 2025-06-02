package com.example.firebase.remoteDataSource.realtimeDatabase

interface WithRemoteSyncStatusChecker {

    /**
     * Checks whether there are any records on the remote that have been updated
     * after the given local pull timestamp.
     *
     * @param since The last known local pull timestamp (in millis)
     * @param key Foreign key that can be either UserId or VehicleId depends on realisation
     * @return True if remote has newer vehicle, settings or refuels records; false otherwise
     */
    suspend fun shouldFetch(since: Long, key: String): Boolean

}