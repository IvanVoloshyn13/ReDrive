package com.example.firebase.remoteDataSource.realtimeDatabase

import com.example.firebase.remoteDataSource.di.RefuelsReference
import com.example.firebase.remoteDataSource.models.RefuelDto
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface RemoteRefuelSource : WithRemoteSyncStatusChecker {
    override suspend fun shouldFetch(since: Long, key: String): Boolean
    suspend fun fetchRefuels(since: Long, userId: String): List<RefuelDto>
    suspend fun sendRefuels(refuels: List<RefuelDto>, userId: String, since: Long): List<RefuelDto>
}

class RemoteRefuelSourceImpl @Inject constructor(
    @RefuelsReference private val databaseReference: DatabaseReference
) : RemoteRefuelSource {
    override suspend fun shouldFetch(since: Long, key: String): Boolean {
        return try {
            val snapshot = databaseReference.child(getCurrentUserPath(key))
                .orderByChild(Constants.UPLOAD_AT)
                .startAfter(since.toDouble())
                .limitToFirst(1)
                .get()
                .await()

            snapshot.children.any { dataSnap ->
                val uploadAt =
                    dataSnap.child(Constants.UPLOAD_AT).getValue(Long::class.java) ?: return false
                uploadAt > since
            }
        } catch (e: DatabaseException) {
            throw RemoteDatabaseException(e.message)
        }

    }

    override suspend fun fetchRefuels(since: Long, userId: String): List<RefuelDto> {
        val path = getCurrentUserPath(userId)
        return try {
            val snapshot = databaseReference.child(path)
                .orderByChild(Constants.UPLOAD_AT)
                .startAfter(since.toDouble())
                .get()
                .await()

            snapshot.children.map { dataSnap ->
                dataSnap.getValue(RefuelDto::class.java) ?: return emptyList()
            }
        } catch (e: DatabaseException) {
            throw RemoteDatabaseException(e.message)
        }
    }

    override suspend fun sendRefuels(
        refuels: List<RefuelDto>,
        userId: String,
        since: Long
    ): List<RefuelDto> {
        val updates = mutableMapOf<String, Any>()
        val userPath = getCurrentUserPath(userId)
        val dtos = ArrayList<RefuelDto>()

        refuels.forEach { refuelDto ->
            val refMap = refuelDto.toMapWithServerTimeStamp()
            val path = "$userPath/${refuelDto.vehicleId}"
            updates[path] = refMap
        }
        return try {
            databaseReference.updateChildren(updates).await()

            val snapshot = databaseReference.child(userId)
                .orderByChild(Constants.UPLOAD_AT)
                .startAfter(since.toDouble())
                .get()
                .await()

            snapshot.children.forEach { dataSnap ->
                val dto = dataSnap.getValue(RefuelDto::class.java) ?: return@forEach
                dtos.add(dto)
            }
            dtos
        } catch (e: DatabaseException) {
            throw RemoteDatabaseException(e.message)
        }

    }
}