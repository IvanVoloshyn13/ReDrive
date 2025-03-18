package com.example.localedatasource.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class,VehicleEntity::class],
    version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUsersDao(): UsersDao
    abstract fun getVehiclesDao(): VehiclesDao
}