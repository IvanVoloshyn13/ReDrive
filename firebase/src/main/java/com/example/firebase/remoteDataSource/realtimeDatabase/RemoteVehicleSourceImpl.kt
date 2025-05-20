package com.example.firebase.remoteDataSource.realtimeDatabase

import android.util.Log
import com.example.firebase.remoteDataSource.di.VehicleReference
import com.example.firebase.remoteDataSource.models.VehicleDto
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteVehicleSourceImpl @Inject constructor(
    @VehicleReference private val vehicleReference: DatabaseReference
) : RemoteVehicleSource {

    override suspend fun shouldDownload(since: Long, currentUserId: String): Boolean {
        return try {
            val snap = vehicleReference
                .orderByChild("userId")
                .equalTo(currentUserId)
                .get()
                .await()

            snap.children.any { data ->
                val updatedAt = data.child("uploadAt").getValue(Long::class.java) ?: 0L
                updatedAt > since
            }
        } catch (e: Exception) {
            false
        }

    }

    override suspend fun downloadVehicles(uUid: String, since: Long): List<VehicleDto> {
        val vehiclesDto: ArrayList<VehicleDto> = ArrayList()
        return try {
            val snap = vehicleReference
                .orderByChild("userId")
                .equalTo(uUid)
                .get()
                .await()

            snap.children.forEach { data ->
                val vehicle = data.getValue(VehicleDto::class.java) ?: return emptyList()
                if (vehicle.uploadAt > since) {
                    vehiclesDto.add(vehicle)
                }
            }
            vehiclesDto
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun uploadVehicles(vehicles: List<VehicleDto>) {
        vehicles.forEach {
            try {
                val vehicleWithTimestamp = it.toMap()
                vehicleReference.child(it.id.toString()).setValue(vehicleWithTimestamp).await()
            } catch (e: FirebaseException) {
                throw e
            }
        }
    }

}

