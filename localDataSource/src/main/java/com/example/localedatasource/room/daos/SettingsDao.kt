package com.example.localedatasource.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.localedatasource.room.entity.UnitPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_preferences WHERE vehicle_id=:vehicleId LIMIT 1")
    fun getUnitPreferencesByCurrentVehicleId(vehicleId: String): Flow<UnitPreferencesEntity>

    @Query("SELECT date_format_pattern_key FROM app_preferences where vehicle_id =:vehicleId")
    suspend fun getDateFormatPatternKey(vehicleId: String): String

    @Query("SELECT avg_consumption_key FROM app_preferences where vehicle_id =:vehicleId")
    suspend fun getAvgConsumptionKey(vehicleId: String): String

    @Query("SELECT distanceKey FROM app_preferences where vehicle_id =:vehicleId")
    suspend fun getDistanceKey(vehicleId: String): String

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePreferences(settings: UnitPreferencesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreferences(settings: UnitPreferencesEntity)

    @Query("SELECT created_at from app_preferences WHERE vehicle_id =:vehicleId")
    suspend fun syncMetaData(vehicleId: String): Long?


    @Query(
        "SELECT COALESCE(MAX(Pref.created_at),0) " +
                "FROM app_preferences AS Pref" +
                " INNER JOIN vehicles AS Veh ON Pref.vehicle_id = Veh.id" +
                " WHERE Veh.user_id =:currentUserId"
    )
     fun since(currentUserId: String): Flow<Long>

    @Query(
        "SELECT EXISTS " +
                "( SELECT 1  FROM app_preferences AS Pref " +
                "INNER JOIN vehicles AS Veh ON Pref.vehicle_id = Veh.id " +
                " WHERE Veh.user_id =:currentUserId AND Pref.sync_status =0 ) "
    )
    fun hasPending(currentUserId: String): Flow<Boolean>

    @Query(
        "SELECT Pref.* FROM app_preferences AS Pref " +
                "INNER JOIN vehicles AS Veh ON Pref.vehicle_id = Veh.id " +
                " WHERE Veh.user_id = :currentUserId AND Pref.sync_status=0"
    )
    fun getPending(currentUserId: String): List<UnitPreferencesEntity>
}