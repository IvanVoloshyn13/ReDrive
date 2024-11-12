package voloshyn.android.data.localeStorage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.localeStorage.room.entities.VehicleEntity

@Dao
interface VehiclesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(vehicle: VehicleEntity):Long

    @Query("DELETE from vehicles WHERE id=:id ")
    suspend fun delete(id: Long)

    @Query("SELECT * from vehicles")
     fun getAll(): Flow<List<VehicleEntity>>

     @Query("SELECT * FROM vehicles WHERE id=:vehicleId ")
     fun currentVehicle(vehicleId:Long):Flow<VehicleEntity>
}