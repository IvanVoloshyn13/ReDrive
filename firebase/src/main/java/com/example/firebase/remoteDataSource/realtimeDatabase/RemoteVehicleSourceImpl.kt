package com.example.firebase.remoteDataSource.realtimeDatabase

import com.example.firebase.remoteDataSource.di.VehicleReference
import com.example.firebase.remoteDataSource.models.VehicleDto
import com.example.firebase.remoteDataSource.realtimeDatabase.Constants.UPLOAD_AT
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface RemoteVehicleSource : WithRemoteSyncStatusChecker {
    override suspend fun shouldFetch(since: Long, key: String): Boolean
    suspend fun fetchVehicles(userId: String, since: Long): List<VehicleDto>
    suspend fun sendVehicles(
        vehicles: List<VehicleDto>, userId: String, since: Long
    ): List<VehicleDto>
}

class RemoteVehicleSourceImpl @Inject constructor(
    @VehicleReference private val vehicleReference: DatabaseReference,
) : RemoteVehicleSource {
    override suspend fun shouldFetch(since: Long, key: String): Boolean {
        return try {
            val snapshot = vehicleReference
                .child(getCurrentUserPath(key))
                .orderByChild(UPLOAD_AT)
                .limitToFirst(1)
                .get()
                .await()

            snapshot.children.any { data ->
                val uploadAt = data.child(UPLOAD_AT).getValue(Long::class.java) ?: 0L
                uploadAt > since
            }
        } catch (e: DatabaseException) {
            false
        }
    }

    override suspend fun fetchVehicles(userId: String, since: Long): List<VehicleDto> {
        val vehiclesDto: ArrayList<VehicleDto> = ArrayList()
        return try {
            val snap = vehicleReference
                .child(getCurrentUserPath(userId))
                .orderByChild(UPLOAD_AT)
                .startAfter(since.toDouble())
                .get()
                .await()

            snap.children.forEach { data ->
                val vehicle = data.getValue(VehicleDto::class.java) ?: return emptyList()
                vehiclesDto.add(vehicle)
            }
            vehiclesDto
        } catch (e: DatabaseException) {
            throw RemoteDatabaseException(e.message)
        }
    }

    override suspend fun sendVehicles(
        vehicles: List<VehicleDto>,
        userId: String,
        since: Long
    ): List<VehicleDto> {
        val updates = mutableMapOf<String, Any>()
        val userPath = getCurrentUserPath(userId)
        val vehiclesDto: ArrayList<VehicleDto> = ArrayList()

        vehicles.forEach {
            val vehicleMap = it.toMapWithServerTimeStamp()
            val vehiclePath = "$userPath/${it.id}"
            updates[vehiclePath] = vehicleMap
        }

        try {
            vehicleReference.updateChildren(updates).await()
        } catch (e: DatabaseException) {
            throw RemoteDatabaseException(e.message)
        }

        val snap = vehicleReference
            .child(getCurrentUserPath(userId))
            .orderByChild(UPLOAD_AT)
            .startAfter(since.toDouble())
            .get()
            .await()

        snap.children.map {
            val dto = it.getValue(VehicleDto::class.java) ?: return emptyList()
            vehiclesDto.add(dto)
        }
        return vehiclesDto
    }

}

