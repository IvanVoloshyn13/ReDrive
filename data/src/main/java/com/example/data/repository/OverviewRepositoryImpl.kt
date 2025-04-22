package com.example.data.repository

import com.example.data.mappers.toRefuel
import com.example.domain.model.Refuel
import com.example.domain.repository.OverviewRepository
import com.example.localedatasource.room.daos.RefuelDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class OverviewRepositoryImpl @Inject constructor(
    private val refuelDao: RefuelDao
) : OverviewRepository {

    override fun observeTravelledDistance(vehicleId: Long): Flow<Int?> {
        return refuelDao.observeTravelledDistance(vehicleId)
    }

    override fun observeFullAmountSum(vehicleId: Long): Flow<Double?> {
        return refuelDao.observeFullAmountSum(vehicleId)
    }

    override fun observePaymentsSum(vehicleId: Long): Flow<Double?> {
        return refuelDao.observePaymentSum(vehicleId)
    }

    override fun observeLastRefuelWithPreviousOdometerReading(vehicleId: Long): Flow<Pair<Refuel?, Int?>> {
        return combine(
            refuelDao.observeLastRefuel(vehicleId),
            refuelDao.observeSecondLastOdometerReading(vehicleId)
        ) { ref, odo ->
            if (odo == ref?.odometer) return@combine Pair(null, null)
            Pair(first = ref?.toRefuel(), second = odo)
        }
    }

}