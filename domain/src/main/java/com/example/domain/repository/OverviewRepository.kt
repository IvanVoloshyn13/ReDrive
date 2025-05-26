package com.example.domain.repository

import com.example.domain.model.Refuel
import kotlinx.coroutines.flow.Flow

interface OverviewRepository {
    fun observeTravelledDistance(vehicleId: String): Flow<Int?>
    fun observeFuelAmountSum(vehicleId: String): Flow<Double?>
    fun observePaymentsSum(vehicleId: String): Flow<Double?>
    fun observeLastRefuel(vehicleId: String): Flow<Refuel?>
    suspend fun fetchSecondLastOdometerReading(vehicleId: String): Int?
}