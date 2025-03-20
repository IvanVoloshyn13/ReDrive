package com.example.data.repository

import com.example.data.toEntity
import com.example.data.toVehicle
import com.example.domain.VehicleException
import com.example.domain.model.Vehicle
import com.example.domain.model.VehicleType
import com.example.domain.repository.VehiclesRepository
import com.example.localedatasource.dataStore.AppVehiclePreferences
import com.example.localedatasource.room.VehiclesDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VehiclesRepositoryImpl @Inject constructor(
    private val appVehiclePreferences: AppVehiclePreferences,
    private val vehiclesDao: VehiclesDao
) : VehiclesRepository {

    override suspend fun addNewVehicle(uUid: String, vehicle: Vehicle): Long {
        return vehiclesDao.addVehicle(vehicle.toEntity(uUid))
    }

    override suspend fun editVehicle(uUid: String, vehicle: Vehicle) {
        vehiclesDao.updateVehicle(vehicle.toEntity(uUid))
    }

    override suspend fun deleteVehicle(vehicleId: Long) {
        vehiclesDao.deleteVehicle(vehicleId)
    }

    override suspend fun confirmCurrentVehicleDelete(vehicleId: Long) {
        vehiclesDao.deleteVehicle(vehicleId)
    }

    override suspend fun setVehicleAsCurrent(vehicleId: Long) {
        appVehiclePreferences.setCurrentVehicleId(vehicleId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeCurrentVehicle(): Flow<Vehicle?> {
        return appVehiclePreferences.observeCurrentVehicleId().flatMapLatest { vehicleId ->
            if (vehicleId != null) {
                vehiclesDao.observeCurrentVehicle(vehicleId).map {
                    Vehicle(
                        id = it.id,
                        name = it.name, initialOdometerValue = it.initialOdometerValue,
                        type = VehicleType.valueOf(it.vehicleType)
                    )
                }
            } else throw VehicleException.NoCurrentVehicleException()
        }
    }

    override fun observeVehicles(userId: String): Flow<List<Vehicle>> {
        return vehiclesDao.observeVehicles(userId).map {
            return@map it.map { entity ->
                entity.toVehicle()
            }
        }
    }
}