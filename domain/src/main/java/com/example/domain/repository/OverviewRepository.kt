package com.example.domain.repository

import com.example.domain.model.Refuel
import kotlinx.coroutines.flow.Flow

interface OverviewRepository {
    fun observeTravelledDistance(vehicleId: Long): Flow<Int?>
    fun observeFullAmountSum(vehicleId: Long): Flow<Double?>
    fun observePaymentsSum(vehicleId: Long): Flow<Double?>

    //Transaction, first request return Refuel, second-odometerValue and create a Pair
    fun observeLastRefuelWithPreviousOdometerReading(vehicleId: Long): Flow<Pair<Refuel?, Int?>>
}