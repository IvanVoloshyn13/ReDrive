package com.example.localedatasource.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.localedatasource.room.entity.SettingsEntity
import com.example.localedatasource.room.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiclesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVehicle(vehicle: VehicleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSettings(settings: SettingsEntity)

    @Transaction
    suspend fun addVehicleWithSettings(vehicle: VehicleEntity, settings: SettingsEntity): Long {
        val vehicleId = addVehicle(vehicle)
        val settingsWithVehicleId = settings.copy(
            vehicleId = vehicleId
        )
        addSettings(settingsWithVehicleId)
        return vehicleId
    }

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity)

    @Query("DELETE from vehicles WHERE id=:vehicleId ")
    suspend fun deleteVehicle(vehicleId: Long)

    @Query("SELECT * from vehicles WHERE user_id=:userId")
    fun observeVehicles(userId: String): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE id=:vehicleId ")
    fun observeCurrentVehicle(vehicleId: Long): Flow<VehicleEntity>


}