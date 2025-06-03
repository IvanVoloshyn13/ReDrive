package com.example.data.worker.refuels

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.worker.WorkSchedulerImpl.Companion.CURRENT_USER_ID_KEY
import com.example.domain.model.SyncStatus
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteRefuelSource
import com.example.localedatasource.room.daos.RefuelDao
import com.example.localedatasource.room.entity.RefuelEntity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FetchRefuelsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val refuelDao: RefuelDao,
    private val remoteRefuelSource: RemoteRefuelSource,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            val userId = inputData.getString(CURRENT_USER_ID_KEY)!!
            val since = refuelDao.since(userId)
            val shouldFetch = remoteRefuelSource.shouldFetch(since, userId)
            if (!shouldFetch) return Result.success()
            val dtos = remoteRefuelSource.fetchRefuels(since, userId)
            val entities = dtos.map { ref ->
                RefuelEntity(
                    id = ref.id,
                    vehicleId = ref.vehicleId,
                    date = ref.refuelDate,
                    odometer = ref.odometerReading,
                    fuelVolume = ref.fuelAmount,
                    unitPrice = ref.pricePerUnit,
                    notes = ref.notes,
                    fullTank = ref.fullTank,
                    missedPrevious = ref.missedPrevious,
                    syncStatus = SyncStatus.SYNCED.code,
                    createdAt = ref.uploadAt
                )
            }
            entities.forEach {
                refuelDao.saveRefuel(it)
            }
            Result.success()
        } catch (e: FirebaseNetworkException) {
            Result.retry() // network problem
        } catch (e: FirebaseException) {
            Result.failure() // permission denied, invalid data, etc.
        } catch (e: Exception) {
            Result.retry()
        }
    }

}