package com.example.data.repository

import android.util.Log
import com.example.data.mappers.toRefuel
import com.example.domain.model.Refuel
import com.example.domain.repository.OverviewRepository
import com.example.localedatasource.room.daos.RefuelDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
            Log.d("ODOMETER", "${ref?.odometer } ${odo.toString()}")
            if (odo == ref?.odometer) return@combine Pair(null, null)
            Log.d("ODOMETER", "here")
            Pair(first = ref?.toRefuel(), second = odo)
        }
    }

//    private fun tada(){
//        refuelDao.observeLastRefuel(vehicleId).filterNotNull().flatMapLatest {ref->
//            refuelDao.observeSecondLastOdometerReading(vehicleId).map {
//                Pair(first = ref.toRefuel(), it)
//            }
//        }
//    }
}