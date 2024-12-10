package voloshyn.android.domain.useCase.settings

import voloshyn.android.domain.models.AppSettings
import voloshyn.android.domain.repository.AppSettingsRepository

class SaveSettingsUseCase(private val repository: AppSettingsRepository) {

    suspend fun invoke(appSettings: AppSettings) {
        repository.saveAppSettings(
            appSettings = appSettings
        )
    }

}