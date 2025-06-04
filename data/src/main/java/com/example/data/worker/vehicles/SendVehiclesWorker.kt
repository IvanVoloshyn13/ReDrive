package com.example.data.worker.vehicles

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.worker.WorkSchedulerImpl.Companion.CURRENT_USER_ID_KEY
import com.example.domain.model.SyncStatus
import com.example.firebase.remoteDataSource.models.VehicleDto
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteDatabaseException
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteVehicleSource
import com.example.localedatasource.room.daos.VehiclesDao
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SendVehiclesWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val vehiclesDao: VehiclesDao,
    private val remoteVehicleSource: RemoteVehicleSource,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            val userId = inputData.getString(CURRENT_USER_ID_KEY)!!
            val since = vehiclesDao.since(userId)
            val entities = vehiclesDao.getPendingVehicles(userId)
            val vehiclesDto = entities.map {
                VehicleDto(
                    id = it.id,
                    userId = userId,
                    name = it.name,
                    initialOdometerValue = it.initialOdometerValue,
                    type = it.vehicleType
                )
            }
            val dtosWithTimeStamp =
                remoteVehicleSource.sendVehicles(vehiclesDto, userId, since = since)
            entities.forEach { ent ->
                val ts = dtosWithTimeStamp.find { it.id == ent.id }?.uploadAt ?: 0L
                vehiclesDao.updateVehicle(
                    ent.copy(
                        syncStatus = SyncStatus.SYNCED.code,
                        createdAt = ts
                    )
                )
            }
            Result.success()
        } catch (e: RemoteDatabaseException) {
            Result.retry()
        } catch (e: FirebaseNetworkException) {
            Result.retry()
        } catch (e: FirebaseException) {
            Result.failure()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}