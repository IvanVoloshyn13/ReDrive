package com.example.domain.sync

interface RefuelsSyncStatusChecker {

    /**
     * Checks if newer refuel records exist remotely compared to the last local pull timestamp.
     * Use this on the splash screen if the user is signed in.
     */
    suspend fun hasRefuelsToSync(): Boolean
}