package com.example.domain.repository

import com.example.domain.model.Refuel
import kotlinx.coroutines.flow.Flow

interface OverviewRepository {
    fun observeTravelledDistance(vehicleId: Long): Flow<Int?>
    fun observeFullAmountSum(vehicleId: Long): Flow<Double?>
    fun observePaymentsSum(vehicleId: Long): Flow<Double?>
    fun observeLastRefuelWithPreviousOdometerReading(vehicleId: Long): Flow<Pair<Refuel?, Int?>>
}