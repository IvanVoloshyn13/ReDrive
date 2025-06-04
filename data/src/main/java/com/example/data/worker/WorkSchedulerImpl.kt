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
import com.example.data.worker.refuels.FetchRefuelsWorker
import com.example.data.worker.refuels.SendRefuelWorker
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

    override fun enqueueFetchDataChain(userId: String) {
        val fVehiclesRequest = getFetchVehiclesRequest(userId)
        val fPreferencesRequest = getFetchPrefsRequest(userId)
        val fRefuelRequest = getFetchRefuelsRequest(userId)
        workManager.beginUniqueWork(
            UniqueWorkName.DATA_SYNC_WORK,
            ExistingWorkPolicy.KEEP,
            fVehiclesRequest
        ).then(listOf(fPreferencesRequest, fRefuelRequest))
            .enqueue()
    }

    private fun getFetchVehiclesRequest(userId: String): OneTimeWorkRequest {
        val builder = OneTimeWorkRequestBuilder<FetchVehiclesWorker>()
        return fetchWorkRequest(userId, builder)
    }

    private fun getFetchPrefsRequest(userId: String): OneTimeWorkRequest {
        val builder = OneTimeWorkRequestBuilder<FetchPrefsWorker>()
        return fetchWorkRequest(userId, builder)
    }

    private fun getFetchRefuelsRequest(userId: String): OneTimeWorkRequest {
        val builder = OneTimeWorkRequestBuilder<FetchRefuelsWorker>()
        return fetchWorkRequest(userId, builder)
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


    override fun enqueueSendVehicles(userId: String) {
        val builder = OneTimeWorkRequestBuilder<SendVehiclesWorker>()
        val request = sendWorkRequest(userId, builder)

        workManager.enqueueUniqueWork(
            "${UniqueWorkName.SEND_VEHICLE}-$userId",
            ExistingWorkPolicy.KEEP,
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

    override fun enqueueSendRefuels(userId: String) {
        val builder = OneTimeWorkRequestBuilder<SendRefuelWorker>()
        val request = sendWorkRequest(userId, builder)
        workManager.enqueueUniqueWork(
            "${UniqueWorkName.SEND_REFUELS}-$userId",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    companion object {
        const val CURRENT_USER_ID_KEY = "current_user_id_key"

        object UniqueWorkName {
            const val SEND_VEHICLE = "send_vehicle"
            const val SEND_PREFERENCES = "send_vehicle_prefs"
            const val SEND_REFUELS = "send_vehicle_refuels"
            const val DATA_SYNC_WORK = "data_sync_work"
        }
    }

}