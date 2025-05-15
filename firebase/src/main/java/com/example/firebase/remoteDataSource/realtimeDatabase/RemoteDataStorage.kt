package com.example.firebase.remoteDataSource.realtimeDatabase

interface RemoteDataStorage {

    /**
     * Checks whether there are any refuel records on the remote that have been updated
     * after the given local pull timestamp.
     *
     * @param since The last known local pull timestamp (in millis)
     * @return True if remote has newer refuel records; false otherwise
     */
    suspend fun hasRefuelsUpdates(since: Long, currentUserId: String): Boolean

    /**
     * Checks whether there are any vehicle records on the remote that have been updated
     * after the given local pull timestamp.
     *
     * @param since The last known local pull timestamp (in millis)
     * @return True if remote has newer vehicle records; false otherwise
     */
    suspend fun hasVehiclesUpdates(since: Long,currentUserId: String): Boolean
}