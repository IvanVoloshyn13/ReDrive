package com.example.localedatasource.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.localedatasource.room.daos.RefuelDao
import com.example.localedatasource.room.daos.SettingsDao
import com.example.localedatasource.room.daos.UsersDao
import com.example.localedatasource.room.daos.VehiclesDao
import com.example.localedatasource.room.entity.RefuelEntity
import com.example.localedatasource.room.entity.UnitPreferencesEntity
import com.example.localedatasource.room.entity.UserEntity
import com.example.localedatasource.room.entity.VehicleEntity

@Database(
    entities = [UserEntity::class, VehicleEntity::class, UnitPreferencesEntity::class, RefuelEntity::class],
    version = 3, exportSchema = true,
    autoMigrations = [AutoMigration(
        from = 1, to = 2
    )]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUsersDao(): UsersDao
    abstract fun getVehiclesDao(): VehiclesDao
    abstract fun getSettingsDao(): SettingsDao
    abstract fun getRefuelDao(): RefuelDao

    companion object {
        val migration2To3 = object : Migration(startVersion = 2, endVersion = 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.beginTransaction()
                try {
                    //1
                    db.execSQL(createNewRefuelTable())
                    //2
                    db.execSQL(copyRefuelData())

                    //3
                    db.execSQL("DROP TABLE refuels")
                    //4
                    db.execSQL("ALTER TABLE tmp_refuels RENAME TO refuels")
                    //5
                    db.execSQL("CREATE INDEX index_refuels_vehicle_id ON refuels(vehicle_id)")
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            private fun createNewRefuelTable(): String {
                return """
                  CREATE TABLE IF NOT EXISTS "tmp_refuels" (
                  	"id"	INTEGER NOT NULL,
                  	"vehicle_id"	TEXT NOT NULL,
                  	"date"	INTEGER NOT NULL,
                  	"odometer"	INTEGER NOT NULL,
                  	"fuel_volume"	REAL NOT NULL,
                  	"unit_price"	REAL NOT NULL,
                  	"notes"	TEXT NOT NULL DEFAULT '',
                  	"full_tank"	INTEGER NOT NULL,
                  	"missed_previous"	INTEGER NOT NULL,
                  	"sync_status"	INTEGER NOT NULL DEFAULT 0,
                  	"created_at"	INTEGER NOT NULL DEFAULT 0,
                  	PRIMARY KEY("id"),
                  	FOREIGN KEY("vehicle_id") REFERENCES "vehicles"("id") 
                  ON UPDATE CASCADE ON DELETE CASCADE
                  )
                """.trimIndent()
            }

            private fun copyRefuelData(): String {
                return """
                    INSERT INTO "tmp_refuels" (id,vehicle_id,date,odometer,fuel_volume,unit_price,
                    notes,full_tank,missed_previous,sync_status,created_at)
                    
                    SELECT id,vehicle_id,date,odometer,fuel_volume,unit_price,
                    notes,full_tank,missed_previous,sync_status,created_at from refuels
                """.trimIndent()
            }
        }


    }

}