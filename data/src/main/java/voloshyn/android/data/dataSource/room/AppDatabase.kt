package voloshyn.android.data.dataSource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import voloshyn.android.data.dataSource.room.dao.AccountsDao
import voloshyn.android.data.dataSource.room.dao.VehiclesDao
import voloshyn.android.data.dataSource.room.entities.AccountEntity
import voloshyn.android.data.dataSource.room.entities.VehicleEntity

@Database(entities = [AccountEntity::class, VehicleEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountDao(): AccountsDao

    abstract fun getVehiclesDao(): VehiclesDao
}