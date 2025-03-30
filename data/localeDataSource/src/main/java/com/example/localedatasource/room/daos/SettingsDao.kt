package com.example.localedatasource.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.localedatasource.room.entity.DateFormatPattern
import com.example.localedatasource.room.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE vehicle_id=:vehicleId LIMIT 1")
    fun getSettingsByCurrentVehicleId(vehicleId: Long): Flow<SettingsEntity>

    @Query("SELECT date_format_pattern FROM app_settings")
    suspend fun getDateFormatPattern(): DateFormatPattern

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: SettingsEntity)
}