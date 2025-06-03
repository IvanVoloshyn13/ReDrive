package com.example.data.worker.refuels

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.worker.WorkSchedulerImpl
import com.example.domain.model.SyncStatus
import com.example.firebase.remoteDataSource.models.RefuelDto
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteRefuelSource
import com.example.localedatasource.room.daos.RefuelDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SendRefuelWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val refuelDao: RefuelDao,
    private val remoteRefuelSource: RemoteRefuelSource
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val userId =
            inputData.getString(WorkSchedulerImpl.CURRENT_USER_ID_KEY) ?: return Result.failure()
        val updates = refuelDao.getPendingRefuels(userId)
        val dtos = updates.map { ent ->
            RefuelDto(
                id = ent.id,
                vehicleId = ent.vehicleId,
                refuelDate = ent.date,
                odometerReading = ent.odometer,
                fuelAmount = ent.fuelVolume,
                pricePerUnit = ent.unitPrice,
                notes = ent.notes,
                fullTank = ent.fullTank,
                missedPrevious = ent.missedPrevious
            )
        }
        val since = refuelDao.since(userId)
        val refuelsWithTimeStamp = remoteRefuelSource.sendRefuels(dtos, userId, since)

        updates.forEach { ent ->
            val ts = refuelsWithTimeStamp.find { it.id == ent.id }?.uploadAt ?: 0L
            refuelDao.updateRefuel(
                ent.copy(
                    syncStatus = SyncStatus.SYNCED.code,
                    createdAt = ts
                )
            )
        }
        return Result.success()
    }
}