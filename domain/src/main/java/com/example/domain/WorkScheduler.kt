package com.example.domain

/**
 * Responsible for scheduling and triggering background work using WorkManager.
 *
 * This interface abstracts the logic of enqueuing background tasks such as
 * uploading vehicles, syncing data, or any other deferred operations.
 *
 * Implementations should define how and when specific workers are scheduled.
 */
interface WorkScheduler {

    /**
     * Enqueues a one-time work request to push vehicles to remote server for the given user.
     *
     * @param uUid Unique identifier of the user whose vehicles should be uploaded.
     */
    fun enqueueUploadVehicles(uUid: String)

    /**
     * Enqueues a one-time work request to push vehicles to remote server for the given user.
     *
     * @param uUid Unique identifier of the user whose vehicles should be uploaded.
     */
    fun enqueueDownloadVehicles(uUid: String)

}