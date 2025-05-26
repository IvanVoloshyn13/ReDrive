package com.example.localedatasource.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.localedatasource.room.entity.UnitPreferencesEntity
import com.example.localedatasource.room.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiclesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVehicle(vehicle: VehicleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSettings(settings: UnitPreferencesEntity)

    @Transaction
    suspend fun upsertVehicle(vehicle: VehicleEntity) {
        val id = insertIgnore(vehicle)
        if (id == -1L) {
            updateVehicle(vehicle)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(vehicle: VehicleEntity): Long

    @Transaction
    suspend fun addVehicleWithSettings(
        vehicle: VehicleEntity,
        settings: UnitPreferencesEntity
    ) {
        addVehicle(vehicle)
        val settingsWithVehicleId = settings.copy(
            vehicleId = vehicle.id
        )
        addSettings(settingsWithVehicleId)
    }

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity)

    @Query("DELETE from vehicles WHERE id=:vehicleId ")
    suspend fun deleteVehicle(vehicleId: String)

    @Query("SELECT * from vehicles WHERE user_id=:userId")
    fun observeVehicles(userId: String): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE id=:vehicleId ")
    fun observeCurrentVehicle(vehicleId: String): Flow<VehicleEntity>

    @Query("SELECT EXISTS( SELECT 1 FROM vehicles WHERE ( user_id =:userId AND sync_status = 0 ))")
    fun hasPendingVehicles(userId: String): Flow<Boolean>

    @Query("SELECT *  FROM vehicles WHERE ( user_id =:userId AND sync_status = 0 )")
    suspend fun getPendingVehicles(userId: String): List<VehicleEntity>

    @Query("SELECT COALESCE(MAX(created_at),0)  FROM vehicles WHERE user_id =:userId")
   suspend fun since(userId: String): Long


}