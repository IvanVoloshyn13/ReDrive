package com.example.domain.model

data class Refuel(
    val refuelDate: Long,
    val odometerValue: Int,
    val fuelVolume: Double,
    val pricePerUnit: Double,
    val notes: String = "",
    val fullTank: Boolean,
    val missedPrevious: Boolean
)
