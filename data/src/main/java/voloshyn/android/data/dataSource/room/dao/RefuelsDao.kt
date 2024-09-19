package voloshyn.android.data.dataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import voloshyn.android.data.dataSource.room.entities.RefuelEntity
@Dao
interface RefuelsDao {

    @Insert
    suspend fun addRefuel(refuel: RefuelEntity)
}