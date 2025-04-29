package com.example.localedatasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.localedatasource.room.daos.RefuelDao
import com.example.localedatasource.room.daos.SettingsDao
import com.example.localedatasource.room.daos.UsersDao
import com.example.localedatasource.room.daos.VehiclesDao
import com.example.localedatasource.room.entity.RefuelEntity
import com.example.localedatasource.room.entity.UnitPreferencesEntity
import com.example.localedatasource.room.entity.UserEntity
import com.example.localedatasource.room.entity.VehicleEntity

@Database(
    entities = [UserEntity::class, VehicleEntity::class, UnitPreferencesEntity::class,RefuelEntity::class],
    version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUsersDao(): UsersDao
    abstract fun getVehiclesDao(): VehiclesDao
    abstract fun getSettingsDao(): SettingsDao
    abstract fun getRefuelDao(): RefuelDao
}