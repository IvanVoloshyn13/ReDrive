package com.example.data.worker

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.background.systemjob.setRequiredNetworkRequest
import androidx.work.workDataOf
import com.example.data.worker.WorkSchedulerKeys.CURRENT_USER_ID_KEY
import com.example.domain.WorkScheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkSchedulerImpl @Inject constructor(
    private val workManager: WorkManager
) : WorkScheduler {
    override fun enqueueUploadVehicles(uUid: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<UploadVehiclesWorkManager>()
            .setInputData(
                workDataOf(
                    CURRENT_USER_ID_KEY to uUid
                )
            )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                30,
                TimeUnit.SECONDS
            )
            .build()

        workManager.enqueueUniqueWork(
            "upload-vehicles-$uUid",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun enqueueDownloadVehicles(uUid: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<DownloadVehiclesWorkManager>()
            .setInputData(
                workDataOf(
                    CURRENT_USER_ID_KEY to uUid
                )
            )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                30,
                TimeUnit.SECONDS
            )
            .build()

        workManager.enqueueUniqueWork(
            "download-vehicles-$uUid",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

}