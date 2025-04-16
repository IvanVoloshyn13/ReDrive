package com.example.localedatasource.room.daos

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.localedatasource.room.entity.UnitPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE vehicle_id=:vehicleId LIMIT 1")
    fun getUnitPreferencesByCurrentVehicleId(vehicleId: Long): Flow<UnitPreferencesEntity>

    @Query("SELECT date_format_pattern_key FROM app_settings where vehicle_id =:vehicleId")
    suspend fun getDateFormatPatternKey(vehicleId: Long): String

    @Query("SELECT avg_consumption_key FROM app_settings where vehicle_id =:vehicleId")
   suspend fun getAvgConsumptionKey(vehicleId: Long): String

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePreferences(settings: UnitPreferencesEntity)
}