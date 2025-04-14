package com.example.domain.model

data class VehicleWithLogs(
    val vehicle: Vehicle? = null,
    val logs: List<RefuelLog> = emptyList()
)