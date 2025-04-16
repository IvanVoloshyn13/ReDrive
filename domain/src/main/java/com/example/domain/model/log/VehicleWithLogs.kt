package com.example.domain.model.log

import com.example.domain.model.Vehicle

data class VehicleWithLogs(
    val vehicle: Vehicle? = null,
    val logs: List<RefuelLog> = emptyList()
)