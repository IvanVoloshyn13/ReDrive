package com.example.domain.model

data class RefuelLog(
    val id: Long = 0L,
    val date: Long = System.currentTimeMillis(),
    val avgConsumption: LogItem,
    val travelledDistance: LogItem,
    val fuelAmount: LogItem,
    val pricePerUnit: LogItem,
    val payment: LogItem
)

data class LogItem(
    val unit: String = "",
    val value: Double = 0.0
)
