package com.example.domain.model.log

data class RefuelLog(
    val id: Long = 0L,
    val date: String = "",
    val odometerRead: Int,
    val avgConsumption: ValueWithUnit,
    val travelledDistance: String,
    val odometerReading: String,
    val fuelAmount: String,
    val pricePerUnit: String,
    val payment: String
)

data class ValueWithUnit(
    val value: String,
    val unit: String
)
