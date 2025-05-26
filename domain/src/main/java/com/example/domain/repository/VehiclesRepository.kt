package com.example.domain.repository

import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface VehiclesRepository {

    /** Add new vehicle with default settings to local database*/
    suspend fun saveVehicleWithSettings(userId: String, vehicle: Vehicle, unitPreferences: UnitsPreferencesAbbreviation)

    /** Edit vehicle  */
    suspend fun updateVehicle(userId: String, vehicle: Vehicle)

    /** Delete vehicle from database */
    suspend fun deleteVehicle(vehicleId: String)

    /** Delete current vehicle and all related data  from database after user confirm */
    suspend fun confirmCurrentVehicleDelete(vehicleId: String)

    /** Save vehicle user chose in last session to dataStore
     * for which all information will be displaying */
    suspend fun setVehicleAsCurrent(vehicleId: String)

    /** Observe current vehicle from dataStore */
    fun observeCurrentVehicle(): Flow<Vehicle?>

    /**Observe vehicles from database */
    fun observeVehicles(userId: String): Flow<List<Vehicle>>

}