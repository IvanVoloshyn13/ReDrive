package com.example.firebase.remoteDataSource.models

data class FbRefuel(
    val id: Long,
    val vehicleId: Long,
    val refuelTimeStamp: Long,
    val odometerValue: Int,
    val fuelAmount: Double,
    val pricePerUnit: Double,
    val notes: String?,
    val fullTank: Boolean,
    val missedPrevious: Boolean
)