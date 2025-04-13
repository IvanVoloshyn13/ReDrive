package com.example.domain.model

data class Refuel(
    val id:Long=0,
    val refuelDate: Long,
    val odometerValue: Int,
    val fuelAmount: Double,
    val pricePerUnit: Double,
    val notes: String = "",
    val fullTank: Boolean,
    val missedPrevious: Boolean
)
