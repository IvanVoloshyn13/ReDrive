package com.example.firebase.remoteDataSource.realtimeDatabase

import com.example.firebase.remoteDataSource.di.VehicleReference
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteDataStorageImpl @Inject constructor(
    @VehicleReference private val vehicleReference: DatabaseReference
) : RemoteDataStorage {
    override suspend fun hasRefuelsUpdates(since: Long, currentUserId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun hasVehiclesUpdates(since: Long, currentUserId: String): Boolean {
        return try {
            val snap = vehicleReference
                .orderByChild("userId")
                .equalTo(currentUserId)
                .get()
                .await()
            snap.children.any { child ->
                val updatedAt = child.child("updatedAt").getValue(Long::class.java) ?: 0L
                updatedAt > since
            }
        } catch (e: Exception) {
            false
        }

    }

}

