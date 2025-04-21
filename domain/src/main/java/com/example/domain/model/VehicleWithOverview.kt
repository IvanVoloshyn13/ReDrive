package com.example.domain.model

import com.example.domain.model.log.RefuelLog
import com.example.domain.model.log.ValueWithUnit

typealias ValueWithUnitToString = String

data class VehicleWithOverview(
    val vehicle: Vehicle?,
    val avgConsumption: ValueWithUnit?,
    val drivingCost: ValueWithUnit?,
    val summary: Summary?,
    val lastRefuelLog: RefuelLog?
)

data class Summary(
    val travelledDistance: ValueWithUnitToString?,
    val fuelAmountSum: ValueWithUnitToString?,
    val paymentsSum: ValueWithUnitToString?
)