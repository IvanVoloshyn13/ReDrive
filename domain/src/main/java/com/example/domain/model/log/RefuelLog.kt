package com.example.domain.model.log

data class RefuelLog(
    val id: Long = 0L,
    val date: String = "",
    val odometerRead: Int,
    val avgConsumption: Pair<String, String>,
    val travelledDistance: String,
    val odometerReading: String,
    val fuelAmount: String,
    val pricePerUnit: String,
    val payment: String
)

data class LogItem(
    val unit: String = "",
    val value: Double = 0.0
)
