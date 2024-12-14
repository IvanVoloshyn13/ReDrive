package voloshyn.android.data.localeStorage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.localeStorage.room.entities.RefuelEntity

@Dao
interface RefuelsDao {

    @Insert
    suspend fun addRefuel(refuel: RefuelEntity):Long


    @Query("SELECT * FROM refuels WHERE vehicle_id=:currentVehicleId order by odometer ASC  ")
     fun getRefuels(currentVehicleId:Long): Flow<List<RefuelEntity>>
}