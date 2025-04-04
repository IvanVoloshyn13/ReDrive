package com.example.localedatasource.room.daos

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.localedatasource.room.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE vehicle_id=:vehicleId LIMIT 1")
    fun getSettingsByCurrentVehicleId(vehicleId: Long): Flow<SettingsEntity>

    @Query("SELECT date_format_pattern FROM app_settings")
    suspend fun getDateFormatPattern(): String

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSettings(settings: SettingsEntity)
}