package com.example.data.repository

import com.example.data.mappers.toEntity
import com.example.data.mappers.toRefuel
import com.example.domain.model.Refuel
import com.example.domain.repository.RefuelRepository
import com.example.localedatasource.room.daos.RefuelDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override suspend fun getRefuelById(refuelId: Long):Refuel {
      return refuelDao.getRefuelById(refuelId).toRefuel()
    }

    override fun observeRefuels(vehicleId: Long): Flow<List<Refuel>> {
        return refuelDao.observeRefuels(vehicleId).map {
            it.map {entity->
                entity.toRefuel()
            }
        }
    }
}