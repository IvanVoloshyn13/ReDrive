package voloshyn.android.domain.useCase.settings

import voloshyn.android.domain.repository.AppSettingsRepository

class ObserveSettingsUseCase(private val repository: AppSettingsRepository) {

    fun invoke() = repository.observeAppSettings()
}