package com.example.domain.model.log

import com.example.domain.useCase.logs.AvgConsumptionType

data class LogPreferences(
    val datePattern: String,
    val avgConsumptionType: AvgConsumptionType
)