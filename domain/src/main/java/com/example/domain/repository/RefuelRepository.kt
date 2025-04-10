package com.example.domain.repository

import com.example.domain.model.Refuel

interface RefuelRepository {

    suspend fun saveRefuel(refuel: Refuel, vehicleId: Long)

    suspend fun updateRefuel(refuel: Refuel, vehicleId: Long)

    suspend fun deleteRefuel(refuelId: Long)
}