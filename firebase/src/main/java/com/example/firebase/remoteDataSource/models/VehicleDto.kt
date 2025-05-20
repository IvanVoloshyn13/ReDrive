package com.example.firebase.remoteDataSource.models

import com.google.firebase.database.ServerValue


data class VehicleDto(
    val id: Long = 0,
    val userId: String = "",
    val name: String = "",
    val initialOdometerValue: Int = 0,
    val type: String = "",
    val uploadAt: Long = 0L
) {
    fun toMap() = mapOf(
        "id" to this.id,
        "userId" to this.userId,
        "name" to this.name,
        "initialOdometerValue" to this.initialOdometerValue,
        "type" to this.type,
        "uploadAt" to ServerValue.TIMESTAMP
    )
}