package com.example.data.repository

import com.example.data.toEntity
import com.example.domain.model.Refuel
import com.example.domain.repository.RefuelRepository
import com.example.localedatasource.room.daos.RefuelDao
import javax.inject.Inject

class RefuelRepositoryImpl @Inject constructor(
    private val refuelDao: RefuelDao
) : RefuelRepository {
    override suspend fun saveRefuel(refuel: Refuel, vehicleId: Long) {
        refuelDao.saveRefuel(refuel.toEntity(vehicleId))
    }

    override suspend fun updateRefuel(refuel: Refuel, vehicleId: Long) {
        refuelDao.updateRefuel(refuel.toEntity(vehicleId))
    }

    override suspend fun deleteRefuel(refuelId: Long) {
        refuelDao.deleteRefuel(refuelId)
    }
}