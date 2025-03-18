package com.example.localedatasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiclesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVehicle(vehicle: VehicleEntity): Long

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity)

    @Query("DELETE from vehicles WHERE id=:vehicleId ")
    suspend fun deleteVehicle(vehicleId: Long)

    @Query("SELECT * from vehicles")
    fun observeVehicles(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE id=:vehicleId ")
    fun observeCurrentVehicle(vehicleId: Long): Flow<VehicleEntity>

}