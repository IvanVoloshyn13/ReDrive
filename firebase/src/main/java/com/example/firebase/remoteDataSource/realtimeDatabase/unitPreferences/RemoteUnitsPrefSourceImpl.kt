package com.example.firebase.remoteDataSource.realtimeDatabase.unitPreferences

import com.example.firebase.remoteDataSource.di.AppSettingsReference
import com.example.firebase.remoteDataSource.models.UnitsPrefDto
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteDatabaseException
import com.example.firebase.remoteDataSource.realtimeDatabase.WithRemoteSyncStatusChecker.Companion.UPLOAD_AT
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteUnitsPrefSourceImpl @Inject constructor(
    @AppSettingsReference private val reference: DatabaseReference
) : RemoteUnitsPrefSource {
    override suspend fun shouldFetch(since: Long, key: String): Boolean {
        return try {
            val snapshot = reference.child(getChildRef(key))
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
        val snapshot = reference.child(getChildRef(userId))
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
            val userPath = getChildRef(userId)
            val path = "$userPath/${it.vehicleId}"
            updates[path] = it.toMap()
        }

        try {
            reference.updateChildren(updates).await()
        } catch (e: DatabaseException) {
            throw RemoteDatabaseException()
        }

        val dtosWithTs = mutableListOf<UnitsPrefDto>()
        val snap = reference.child(getChildRef(userId))
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

    private fun getChildRef(userId: String) = "user-$userId"
}