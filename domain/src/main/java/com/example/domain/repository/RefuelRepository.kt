package com.example.domain.repository

import com.example.domain.model.Refuel
import kotlinx.coroutines.flow.Flow

interface RefuelRepository {

    suspend fun saveRefuel(refuel: Refuel, vehicleId: Long)

    suspend fun updateRefuel(refuel: Refuel, vehicleId: Long)

    suspend fun deleteRefuel(refuelId: Long)

    suspend fun getRefuelById(refuelId: Long): Refuel

    fun observeRefuels(vehicleId: Long): Flow<List<Refuel>>
}