package com.example.domain.model

data class Refuel(
    val id: Long = 0,
    val refuelTimeStamp: Long = System.currentTimeMillis(),
    val odometerValue: Int = 0,
    val fuelAmount: Double = 0.0,
    val pricePerUnit: Double = 0.0,
    val notes: String = "",
    val fullTank: Boolean = true,
    val missedPrevious: Boolean = false
)
