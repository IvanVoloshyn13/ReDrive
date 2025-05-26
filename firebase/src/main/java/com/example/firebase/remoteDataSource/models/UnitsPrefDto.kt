package com.example.firebase.remoteDataSource.models

import com.google.firebase.database.ServerValue

data class UnitsPrefDto(
    val id: Long = 0,
    val vehicleId: String = "",
    val currencyKey: String = "",
    val distanceKey: String = "",
    val capacityKey: String = "",
    val avgConsumptionKey: String = "",
    val dateFormatPatternKey: String = "",
    val uploadAt: Long = 0L
) {
    fun toMap() = mapOf(
        "id" to this.id,
        "vehicleId" to this.vehicleId,
        "currencyKey" to this.currencyKey,
        "distanceKey" to this.distanceKey,
        "capacityKey" to this.capacityKey,
        "avgConsumptionKey" to this.avgConsumptionKey,
        "dateFormatPatternKey" to this.dateFormatPatternKey,
        "uploadAt" to ServerValue.TIMESTAMP
    )
}