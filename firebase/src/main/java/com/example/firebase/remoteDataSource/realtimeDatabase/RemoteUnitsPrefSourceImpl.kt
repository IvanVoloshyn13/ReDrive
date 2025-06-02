package com.example.firebase.remoteDataSource.realtimeDatabase

import com.example.firebase.remoteDataSource.di.AppSettingsReference
import com.example.firebase.remoteDataSource.models.UnitsPrefDto
import com.example.firebase.remoteDataSource.realtimeDatabase.Constants.UPLOAD_AT
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface RemoteUnitsPrefSource : WithRemoteSyncStatusChecker {
    override suspend fun shouldFetch(since: Long, key: String): Boolean
    suspend fun fetch(userId: String, since: Long): List<UnitsPrefDto>
    suspend fun send(
        userId: String, unitsPrefDto: List<UnitsPrefDto>, since: Long
    ): List<UnitsPrefDto>

}

class RemoteUnitsPrefSourceImpl @Inject constructor(
    @AppSettingsReference private val reference: DatabaseReference
) : RemoteUnitsPrefSource {
    override suspend fun shouldFetch(since: Long, key: String): Boolean {
        return try {
            val snapshot = reference.child(getCurrentUserPath(key))
                .orderByChild(UPLOAD_AT)
                .limitToLast(1)
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

    override suspend fun fetch(userId: String, since: Long): List<UnitsPrefDto> {
        val dtos = mutableListOf<UnitsPrefDto>()
        val snapshot = reference.child(getCurrentUserPath(userId))
            .orderByChild(UPLOAD_AT)
            .startAfter(since.toDouble())
            .get()
            .await()

        snapshot.children.forEach { data ->
            val dto = data.getValue(UnitsPrefDto::class.java) ?: return emptyList()
            dtos.add(dto)
        }
        return dtos
    }

    override suspend fun send(
        userId: String,
        unitsPrefDto: List<UnitsPrefDto>,
        since: Long
    ): List<UnitsPrefDto> {
        val updates = mutableMapOf<String, Any?>()
        unitsPrefDto.forEach {
            val userPath = getCurrentUserPath(userId)
            val path = "$userPath/${it.vehicleId}"
            updates[path] = it.toMap()
        }

        try {
            reference.updateChildren(updates).await()
        } catch (e: DatabaseException) {
            throw RemoteDatabaseException(e.message)
        }

        val dtosWithTs = mutableListOf<UnitsPrefDto>()
        val snap = reference.child(getCurrentUserPath(userId))
            .orderByChild(UPLOAD_AT)
            .startAfter(since.toDouble())
            .get()
            .await()

        snap.children.forEach {
            val dto = it.getValue(UnitsPrefDto::class.java) ?: return emptyList()
            dtosWithTs.add(dto)
        }
        return dtosWithTs
    }

}