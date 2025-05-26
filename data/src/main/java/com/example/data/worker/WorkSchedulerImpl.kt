package com.example.data.worker

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.data.worker.prefs.FetchPrefsWorker
import com.example.data.worker.prefs.SendPrefsWorker
import com.example.data.worker.vehicles.FetchVehiclesWorker
import com.example.data.worker.vehicles.SendVehiclesWorker
import com.example.domain.sync.WorkScheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkSchedulerImpl @Inject constructor(
    private val workManager: WorkManager
) : WorkScheduler {

    private fun sendWorkRequest(
        userId: String,
        builder: OneTimeWorkRequest.Builder
    ): OneTimeWorkRequest {
        return builder
            .setInputData(
                workDataOf(
                    CURRENT_USER_ID_KEY to userId
                )
            )
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                15,
                TimeUnit.SECONDS
            )
            .build()
    }

    private fun fetchWorkRequest(
        userId: String,
        builder: OneTimeWorkRequest.Builder
    ): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        return builder
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    CURRENT_USER_ID_KEY to userId
                )
            )
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                15,
                TimeUnit.SECONDS
            )
            .build()
    }

    override fun enqueueSendVehicles(userId: String) {
        val builder = OneTimeWorkRequestBuilder<SendVehiclesWorker>()
        val request = sendWorkRequest(userId, builder)

        workManager.enqueueUniqueWork(
            "${UniqueWorkName.SEND_VEHICLE}-$userId",
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    override fun enqueueFetchVehicles(userId: String) {
        val builder = OneTimeWorkRequestBuilder<FetchVehiclesWorker>()
        val request = fetchWorkRequest(userId, builder)
        workManager.enqueueUniqueWork(
            UniqueWorkName.FETCH_VEHICLE,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun enqueueSendUnitPreferences(userId: String) {
        val builder = OneTimeWorkRequestBuilder<SendPrefsWorker>()
        val request = sendWorkRequest(userId, builder)
        workManager.enqueueUniqueWork(
            "${UniqueWorkName.SEND_PREFERENCES}-$userId",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun enqueueFetchUnitPreferences(userId: String) {
        val builder = OneTimeWorkRequestBuilder<FetchPrefsWorker>()
        val request = fetchWorkRequest(userId, builder)
        workManager.enqueueUniqueWork(
            UniqueWorkName.FETCH_PREFERENCES,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    companion object {
        const val CURRENT_USER_ID_KEY = "current_user_id_key"

        object UniqueWorkName {
            const val SEND_VEHICLE = "send-vehicle"
            const val FETCH_VEHICLE = "fetch-vehicle"
            const val SEND_PREFERENCES = "send-vehicle-prefs"
            const val FETCH_PREFERENCES = "fetch-vehicle-prefs"
        }
    }

}