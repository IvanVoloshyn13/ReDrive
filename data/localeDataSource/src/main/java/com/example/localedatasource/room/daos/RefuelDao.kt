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

    @Query("SELECT * FROM refuels WHERE id=:refuelId")
    suspend fun getRefuelById(refuelId: Long): RefuelEntity

    @Query("DELETE FROM refuels WHERE id=:id")
    fun deleteRefuel(id: Long)

    @Update
    suspend fun updateRefuel(refuel: RefuelEntity)

    @Query("SELECT * FROM refuels WHERE vehicle_id=:currentVehicleId order by odometer DESC LIMIT 0,1")
    fun observeLastRefuel(currentVehicleId: Long): Flow<RefuelEntity?>

    @Query(
        "SELECT odometer FROM refuels" +
                " WHERE odometer<(SELECT MAX(odometer) FROM refuels " +
                "WHERE vehicle_id=:currentVehicleId) " +
                "ORDER by odometer DESC LIMIT 0,1  "
    )
    fun observeSecondLastOdometerReading(currentVehicleId: Long): Flow<Int?>

    @Query(
        """
    SELECT MAX(r.odometer) - v.initial_odometer_value
    FROM vehicles v
    JOIN refuels r ON v.id = r.vehicle_id
    WHERE v.id =:currentVehicleId
"""
    )
    fun observeTravelledDistance(currentVehicleId: Long): Flow<Int?>

    @Query("SELECT SUM(fuel_volume) FROM refuels WHERE vehicle_id=:currentVehicleId")
    fun observeFullAmountSum(currentVehicleId: Long): Flow<Double?>

    @Query("SELECT SUM(fuel_volume*unit_price) FROM refuels WHERE vehicle_id=:currentVehicleId")
    fun observePaymentSum(currentVehicleId: Long): Flow<Double?>


}