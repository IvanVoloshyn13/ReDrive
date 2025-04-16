package com.example.localedatasource.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.localedatasource.room.entity.RefuelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RefuelDao {

    @Insert
    suspend fun saveRefuel(refuel: RefuelEntity)

    @Query("SELECT * FROM refuels WHERE vehicle_id=:currentVehicleId order by odometer ASC  ")
    fun observeRefuels(currentVehicleId: Long): Flow<List<RefuelEntity>>

    @Query("DELETE FROM refuels WHERE id=:id")
    fun deleteRefuel(id:Long)

    @Update
    suspend fun updateRefuel(refuel: RefuelEntity)
}