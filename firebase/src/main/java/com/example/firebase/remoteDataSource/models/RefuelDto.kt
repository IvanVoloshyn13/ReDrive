package com.example.firebase.remoteDataSource.models

import com.google.firebase.database.ServerValue

data class RefuelDto(
    val id: Long = 0,
    val vehicleId: String = "",
    val refuelDate: Long = 0,
    val odometerReading: Int = 0,
    val fuelAmount: Double = 0.0,
    val pricePerUnit: Double = 0.0,
    val notes: String = "",
    val fullTank: Boolean = true,
    val missedPrevious: Boolean = true,
    val uploadAt: Long = 0L
) {
    fun toMapWithServerTimeStamp() = mapOf(
        "id" to this.id,
        "vehicleId" to this.vehicleId,
        "refuelDate" to this.refuelDate,
        "odometerReading" to this.odometerReading,
        "fuelAmount" to this.fuelAmount,
        "pricePerUnit" to this.pricePerUnit,
        "notes" to this.notes,
        "fullTank" to this.fullTank,
        "missedPrevious" to this.missedPrevious,
        "uploadAt" to ServerValue.TIMESTAMP
    )
}