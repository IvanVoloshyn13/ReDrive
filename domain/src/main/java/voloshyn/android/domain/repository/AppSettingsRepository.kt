package voloshyn.android.domain.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.AppSettings
import voloshyn.android.domain.models.SettingType


interface AppSettingsRepository {

    suspend fun saveAppSettings(appSettings: AppSettings)

    fun observeAppSettings(): Flow<AppSettings>

     fun getItemUnits(type: SettingType): Array<out String>
}