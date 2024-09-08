package voloshyn.android.data.dataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.dataSource.room.entities.VehicleEntity
import voloshyn.android.domain.models.tabs.redrive.Vehicle

@Dao
interface VehiclesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(vehicle: VehicleEntity):Long

    @Query("DELETE from vehicles WHERE id=:id ")
    suspend fun delete(id: Long)

    @Query("SELECT * from vehicles")
     fun getAll(): Flow<List<VehicleEntity>>
}