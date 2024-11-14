package voloshyn.android.data.localeStorage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import voloshyn.android.data.localeStorage.room.dao.AccountsDao
import voloshyn.android.data.localeStorage.room.dao.RefuelsDao
import voloshyn.android.data.localeStorage.room.dao.VehiclesDao
import voloshyn.android.data.localeStorage.room.entities.AccountEntity
import voloshyn.android.data.localeStorage.room.entities.RefuelEntity
import voloshyn.android.data.localeStorage.room.entities.VehicleEntity

@Database(
    entities = [AccountEntity::class, VehicleEntity::class, RefuelEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountDao(): AccountsDao

    abstract fun getVehiclesDao(): VehiclesDao

    abstract fun getRefuelsDao(): RefuelsDao

}