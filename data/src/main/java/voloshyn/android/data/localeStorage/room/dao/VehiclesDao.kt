package voloshyn.android.data.localeStorage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.localeStorage.room.entities.VehicleEntity

@Dao
interface VehiclesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(vehicle: VehicleEntity): Long

    @Update
    suspend fun update(vehicle: VehicleEntity)

    @Query("DELETE from vehicles WHERE id=:vehicleId ")
    suspend fun delete(vehicleId: Long)

    @Query("SELECT * from vehicles")
    fun getAll(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE id=:vehicleId ")
    fun currentVehicle(vehicleId: Long): Flow<VehicleEntity>

    @Query("SELECT COUNT(*)>0 from vehicles where user_id =:uUid  ")
    suspend fun isVehicle(uUid: String): Boolean
}