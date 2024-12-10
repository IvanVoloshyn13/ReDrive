package voloshyn.android.data.localeStorage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import voloshyn.android.data.localeStorage.room.dao.RefuelsDao
import voloshyn.android.data.localeStorage.room.dao.SettingsDao
import voloshyn.android.data.localeStorage.room.dao.UsersDao
import voloshyn.android.data.localeStorage.room.dao.VehiclesDao
import voloshyn.android.data.localeStorage.room.entities.RefuelEntity
import voloshyn.android.data.localeStorage.room.entities.SettingsEntity
import voloshyn.android.data.localeStorage.room.entities.UserEntity
import voloshyn.android.data.localeStorage.room.entities.VehicleEntity

@Database(
    entities = [UserEntity::class, VehicleEntity::class, RefuelEntity::class,SettingsEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountDao(): UsersDao

    abstract fun getVehiclesDao(): VehiclesDao

    abstract fun getRefuelsDao(): RefuelsDao

    abstract fun getSettingsDao(): SettingsDao

}