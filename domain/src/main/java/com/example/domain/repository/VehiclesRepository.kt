package com.example.domain.repository

import com.example.domain.VehicleException
import com.example.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface VehiclesRepository {

    /** Add new vehicle to local database */
    suspend fun addNewVehicle(uUid: String, vehicle: Vehicle): Long

    /** Edit vehicle  */
    suspend fun editVehicle(uUid: String, vehicle: Vehicle)

    /** Delete vehicle from database
     * @throws VehicleException.IsCurrentVehicleException if vehicle trying to be delete is set as Current */
    @Throws(VehicleException.IsCurrentVehicleException::class)
    suspend fun deleteVehicle(vehicleId: Long)

    /** Delete current vehicle and all related data  from database after user confirm */
    suspend fun confirmCurrentVehicleDelete(vehicleId: Long)

    /** Save vehicle user chose in last session to dataStore
     * for which all information will be displaying */
    suspend fun setVehicleAsCurrent(vehicleId: Long)

    /** Observe current vehicle from dataStore */
    fun observeCurrentVehicle(): Flow<Vehicle?>

    /**Observe vehicles from database */
    fun observeVehicles(userId: String): Flow<List<Vehicle>>

}