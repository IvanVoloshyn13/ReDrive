package com.example.data.worker.prefs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.worker.WorkSchedulerImpl.Companion.CURRENT_USER_ID_KEY
import com.example.domain.model.SyncStatus
import com.example.firebase.remoteDataSource.models.UnitsPrefDto
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteDatabaseException
import com.example.firebase.remoteDataSource.realtimeDatabase.unitPreferences.RemoteUnitsPrefSource
import com.example.localedatasource.room.daos.SettingsDao
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SendPrefsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val settingsDao: SettingsDao,
    private val remoteUnitsPrefSource: RemoteUnitsPrefSource,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            val userId = inputData.getString(CURRENT_USER_ID_KEY)!!
            val uploads = settingsDao.getPending(userId)
            val dtos = uploads.map {
                UnitsPrefDto(
                    id = it.id,
                    vehicleId = it.vehicleId,
                    currencyKey = it.currencyKey,
                    distanceKey = it.distanceKey,
                    capacityKey = it.capacityKey,
                    avgConsumptionKey = it.avgConsumptionKey,
                    dateFormatPatternKey = it.dateFormatPatternKey
                )
            }
            val dtosWithTs = remoteUnitsPrefSource.send(userId, dtos, 0)

            uploads.forEach { ent ->
                val ts = dtosWithTs.find { it.id == ent.id }?.uploadAt ?: 0L
                settingsDao.updatePreferences(
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