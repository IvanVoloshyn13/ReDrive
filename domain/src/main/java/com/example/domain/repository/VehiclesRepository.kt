package com.example.domain.repository

import com.example.domain.model.Settings
import com.example.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface VehiclesRepository {

    /** Add new vehicle with default settings to local database*/
    suspend fun saveVehicleWithSettings(uUid: String, vehicle: Vehicle, settings: Settings): Long

    /** Edit vehicle  */
    suspend fun updateVehicle(uUid: String, vehicle: Vehicle)

    /** Delete vehicle from database */
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