package com.example.data.repository

import com.example.data.mappers.toRefuel
import com.example.domain.model.Refuel
import com.example.domain.repository.OverviewRepository
import com.example.localedatasource.room.daos.RefuelDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OverviewRepositoryImpl @Inject constructor(
    private val refuelDao: RefuelDao
) : OverviewRepository {

    override fun observeTravelledDistance(vehicleId: String): Flow<Int?> {
        return refuelDao.observeTravelledDistance(vehicleId)
    }

    override fun observeFuelAmountSum(vehicleId: String): Flow<Double?> {
        return refuelDao.observeFullAmountSum(vehicleId)
    }

    override fun observePaymentsSum(vehicleId: String): Flow<Double?> {
        return refuelDao.observePaymentSum(vehicleId)
    }


    override fun observeLastRefuel(vehicleId: String): Flow<Refuel?> {
        return refuelDao.observeLastRefuel(vehicleId).map {
            it?.toRefuel()
        }
    }

    override suspend fun fetchSecondLastOdometerReading(vehicleId: String): Int? {
        return refuelDao.getSecondLastOdometerReading(vehicleId)
    }

}