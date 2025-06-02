package com.example.firebase.remoteDataSource.models

import com.google.firebase.database.ServerValue

data class RefuelDto(
    val id: Long = 0,
    val vehicleId: Long = 0,
    val refuelDate: Long = 0,
    val odometerReading: Int = 0,
    val fuelAmount: Double = 0.0,
    val pricePerUnit: Double = 0.0,
    val notes: String = "",
    val fullTank: Boolean = true,
    val missedPrevious: Boolean = true
) {
    fun toMapWithServerTimeStamp() = mapOf(
        "id" to id,
        "vehicleId" to vehicleId,
        "refuelDate" to refuelDate,
        "odometerReading" to odometerReading,
        "fuelAmount" to fuelAmount,
        "pricePerUnit" to pricePerUnit,
        "notes" to notes,
        "fullTank" to fullTank,
        "missedPrevious" to missedPrevious,
        "uploadAt" to ServerValue.TIMESTAMP
    )
}