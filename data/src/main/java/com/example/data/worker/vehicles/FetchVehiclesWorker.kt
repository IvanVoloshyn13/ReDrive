package com.example.data.worker.vehicles

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.worker.WorkSchedulerImpl.Companion.CURRENT_USER_ID_KEY
import com.example.domain.model.SyncStatus
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteVehicleSource
import com.example.localedatasource.room.daos.VehiclesDao
import com.example.localedatasource.room.entity.VehicleEntity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FetchVehiclesWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val vehiclesDao: VehiclesDao,
    private val remoteVehicleSource: RemoteVehicleSource,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            val uUid = inputData.getString(CURRENT_USER_ID_KEY)!!
            val since = vehiclesDao.since(uUid)
            val shouldFetch = remoteVehicleSource.shouldFetch(since, uUid)
            if (!shouldFetch) return Result.success()
            val dtos = remoteVehicleSource.fetchVehicles(uUid, since)
            val entities = dtos.map {
                VehicleEntity(
                    id = it.id,
                    userId = uUid,
                    name = it.name,
                    initialOdometerValue = it.initialOdometerValue,
                    vehicleType = it.type,
                    syncStatus = SyncStatus.SYNCED.code,
                    createdAt = it.uploadAt
                )
            }
            entities.forEach {
                vehiclesDao.upsertVehicle(it)
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