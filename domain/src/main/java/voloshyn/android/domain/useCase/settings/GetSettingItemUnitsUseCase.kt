package voloshyn.android.domain.useCase.settings

import voloshyn.android.domain.models.AppUnit
import voloshyn.android.domain.models.SettingType
import voloshyn.android.domain.repository.AppSettingsRepository

class GetSettingItemUnitsUseCase(private val repository: AppSettingsRepository) {
    fun invoke(field: SettingType): Array<AppUnit> {
        val units = repository.getItemUnits(field)
        val appUnits = units.map {
            val unit = it.split("|")[1]
            unit
        }.toTypedArray()
        return appUnits
    }
}