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
    fun observeRefuels(currentVehicleId: String): Flow<List<RefuelEntity>>

    @Query("SELECT * FROM refuels WHERE id=:refuelId")
    suspend fun getRefuelById(refuelId: Long): RefuelEntity

    @Query("DELETE FROM refuels WHERE id=:id")
    fun deleteRefuel(id: Long)

    @Update
    suspend fun updateRefuel(refuel: RefuelEntity)

    @Query("SELECT * FROM refuels WHERE vehicle_id=:currentVehicleId order by odometer DESC LIMIT 0,1")
    fun observeLastRefuel(currentVehicleId: String): Flow<RefuelEntity?>

    @Query(
        """
    SELECT odometer FROM refuels
    WHERE vehicle_id = :currentVehicleId
    ORDER BY odometer DESC
    LIMIT 1 OFFSET 1
"""
    )
    suspend fun getSecondLastOdometerReading(currentVehicleId: String): Int?

    @Query(
        """
    SELECT MAX(r.odometer) - v.initial_odometer_value
    FROM vehicles v
    JOIN refuels r ON v.id = r.vehicle_id
    WHERE v.id =:currentVehicleId
"""
    )
    fun observeTravelledDistance(currentVehicleId: String): Flow<Int?>

    @Query("SELECT SUM(fuel_volume) FROM refuels WHERE vehicle_id=:currentVehicleId")
    fun observeFullAmountSum(currentVehicleId: String): Flow<Double?>

    @Query("SELECT SUM(fuel_volume*unit_price) FROM refuels WHERE vehicle_id=:currentVehicleId")
    fun observePaymentSum(currentVehicleId: String): Flow<Double?>

    @Query(
        """
        SELECT EXISTS (SELECT 1 FROM refuels AS  r 
        INNER JOIN vehicles AS v ON  v.id=r.vehicle_id 
        WHERE v.user_id=:currentUserId AND r.sync_status=0 )
    """
    )
    fun hasPending(currentUserId: String): Flow<Boolean>

    @Query(
        """
        SELECT r.* FROM refuels AS  r 
        INNER JOIN vehicles AS v ON  v.id=r.vehicle_id 
        WHERE v.user_id=:currentUserId AND r.sync_status=0 
    """
    )
    suspend fun getPendingRefuels(currentUserId: String): List<RefuelEntity>

    @Query("""
        SELECT MAX(r.created_at) FROM refuels AS r
        INNER JOIN vehicles as v on v.id=r.vehicle_id
        WHERE v.user_id=:currentUserId
    """)
    suspend fun since(currentUserId: String):Long

}