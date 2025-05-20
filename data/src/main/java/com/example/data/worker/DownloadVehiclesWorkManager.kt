package com.example.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.worker.WorkSchedulerKeys.CURRENT_USER_ID_KEY
import com.example.domain.model.SyncStatus
import com.example.firebase.remoteDataSource.models.VehicleDto
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteVehicleSource
import com.example.localedatasource.room.daos.VehiclesDao
import com.example.localedatasource.room.entity.VehicleEntity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DownloadVehiclesWorkManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val vehiclesDao: VehiclesDao,
    private val remoteVehicleSource: RemoteVehicleSource
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            val uUid = inputData.getString(CURRENT_USER_ID_KEY)!!
            val vehicles = remoteVehicleSource.downloadVehicles(uUid, 0L)
            val vehiclesEntity = vehicles.map {
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
            vehiclesEntity.forEach {
                vehiclesDao.addVehicle(it)
            }
            Result.success()
        } catch (e: FirebaseNetworkException) {
            Result.retry() // network problem
        } catch (e: FirebaseException) {
            Result.failure() // permission denied, invalid data, etc.
        }
    }

}