package com.example.data.worker.prefs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.worker.WorkSchedulerImpl.Companion.CURRENT_USER_ID_KEY
import com.example.domain.model.SyncStatus
import com.example.firebase.remoteDataSource.realtimeDatabase.unitPreferences.RemoteUnitsPrefSource
import com.example.localedatasource.room.daos.SettingsDao
import com.example.localedatasource.room.entity.UnitPreferencesEntity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class FetchPrefsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val settingsDao: SettingsDao,
    private val remoteUnitsPrefSource: RemoteUnitsPrefSource,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            val userId = inputData.getString(CURRENT_USER_ID_KEY)!!
            val since = settingsDao.since(userId).first()
            val shouldFetch = remoteUnitsPrefSource.shouldFetch(since, userId)
            if (!shouldFetch) return Result.success()
            val dtos = remoteUnitsPrefSource.fetch(userId, since)
            val entities = dtos.map { prefs ->
                UnitPreferencesEntity(
                    id = prefs.id,
                    vehicleId = prefs.vehicleId,
                    currencyKey = prefs.currencyKey,
                    distanceKey = prefs.distanceKey,
                    capacityKey = prefs.capacityKey,
                    avgConsumptionKey = prefs.avgConsumptionKey,
                    dateFormatPatternKey = prefs.dateFormatPatternKey,
                    syncStatus = SyncStatus.SYNCED.code,
                    createdAt = prefs.uploadAt
                )
            }
            entities.forEach {
                settingsDao.insertPreferences(it)
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