package com.example.domain.sync

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
     * Enqueues a one-time work chain request to pull vehicles,preferences and refuels from remote server for the given user.
     * Invoke it on SplashScreen
     *
     * @param userId Unique identifier of the user whose vehicles should be send to backend.
     */
    fun enqueueFetchDataChain(userId: String)

    /**
     * Enqueues a one-time work request to push vehicles to remote server for the given user.
     *
     * @param userId Unique identifier of the user whose vehicles should be uploaded.
     */
    fun enqueueSendVehicles(userId: String)

    /**
     * Enqueues a one-time work request to push vehicle prefs to remote server for the given user.
     *
     * @param userId Unique identifier of the user whose vehicle preferences should be uploaded.
     */
    fun enqueueSendUnitPreferences(userId: String)

    /**
     * Enqueues a one-time work request to push refuels to remote server for the given user.
     *
     * @param userId Unique identifier of the user whose vehicle preferences should be uploaded.
     */
    fun enqueueSendRefuels(userId: String)


}