package voloshyn.android.data.localeStorage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.localeStorage.room.entities.SettingsEntity

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE user_id=:userId LIMIT 1")
    fun getSettingsByUserId(userId: String): Flow<SettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: SettingsEntity)
}