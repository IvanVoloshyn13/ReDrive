package com.example.domain.sync

import com.example.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Qualifier

 interface SendDataStatusChecker {

    /**
     * Use this to observe the Room database for any records that still need
     * to be pushed to the backend.
     * @param key - Foreign key that can be either UserId or VehicleId depends on realisation
     *
     * @return a [Flow] emitting if any records with [SyncStatus.PENDING] exist in local
     * database .
     */
    fun shouldSend(key: String): Flow<Boolean>
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class VehiclesSyncChecker

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UnitPreferencesSyncChecker

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RefuelSendChecker