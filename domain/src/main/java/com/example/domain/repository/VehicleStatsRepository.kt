package com.example.domain.repository

import com.example.domain.model.Refuel
import kotlinx.coroutines.flow.Flow

interface VehicleStatsRepository {
    fun observeTravelledDistance(vehicleId: String): Flow<Int?>
    fun observeFuelAmountSum(vehicleId: String): Flow<Double?>
    fun observePaymentsSum(vehicleId: String): Flow<Double?>
    fun observeLastRefuel(vehicleId: String): Flow<Refuel?>
    suspend fun getSecondLastOdometerReading(vehicleId: String): Int?
}