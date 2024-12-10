package voloshyn.android.redrive.presentation.tabs.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.domain.models.AppUnit
import voloshyn.android.domain.models.SettingItem
import voloshyn.android.domain.models.AppSettings
import voloshyn.android.domain.models.SettingType
import voloshyn.android.domain.useCase.settings.ObserveSettingsUseCase
import voloshyn.android.domain.useCase.settings.GetUnitsUseCase
import voloshyn.android.domain.useCase.settings.SaveSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettings: ObserveSettingsUseCase,
    private val getUnits: GetUnitsUseCase,
    private val saveSettings: SaveSettingsUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()


    init {
        viewModelScope.launch {
            getSettings.invoke().collectLatest {
                initSettingsState(it)
            }
        }
    }

    private fun initSettingsState(appSettings: AppSettings) {
        val currency = createSettingsItem(appSettings.currency)
        val capacity = createSettingsItem(appSettings.capacity)
        val distance = createSettingsItem(appSettings.distance)
        val avgConsumption = createSettingsItem(appSettings.avgConsumption)
        val formatOfDate = createSettingsItem(appSettings.dateFormat)


        _state.update {
            it.copy(
                capacity = capacity,
                currency = currency,
                avgConsumption = avgConsumption,
                distance = distance,
                formatOfDate = formatOfDate
            )
        }

    }

    private fun createSettingsItem(item: SettingItem): SettingsStateItem {
        return SettingsStateItem(
            id = item.id,
            field = item.type,
            settingsName = item.name,
            settingsUnitValue = item.valueUnit,
            icon = getSettingsItemDrawable(item.type)
        )
    }

    private fun getSettingsItemDrawable(field: SettingType): Int {
        return when (field) {
            SettingType.Currency -> R.drawable.icon_settings_currency
            SettingType.Capacity -> R.drawable.icon_settings_capacity
            SettingType.Distance -> R.drawable.icon_settings_distance
            SettingType.AvgConsumption -> R.drawable.icon_settings_avg_consumption
            SettingType.FormatOfDate -> R.drawable.icon_settings_date
            SettingType.Default -> R.drawable.ic_settings
        }
    }

    fun getUnits(field: SettingType): Array<AppUnit> {
        return getUnits.invoke(field)
    }

    fun updateSettingsState(field: SettingType, newUnit: String, position:Int) {
        _state.update {

            when (field) {
                SettingType.Currency -> it.copy(
                    currency = it.currency.copy(id = position.toString(), settingsUnitValue = newUnit)
                )

                SettingType.Capacity -> it.copy(
                    capacity = it.capacity.copy(id = position.toString(),settingsUnitValue = newUnit)
                )

                SettingType.Distance -> it.copy(
                    distance = it.distance.copy(id = position.toString(),settingsUnitValue = newUnit)
                )

                SettingType.AvgConsumption -> it.copy(
                    avgConsumption = it.avgConsumption.copy(id = position.toString(),settingsUnitValue = newUnit)
                )

                SettingType.FormatOfDate -> it.copy(
                    formatOfDate = it.formatOfDate.copy(id = position.toString(),settingsUnitValue = newUnit)
                )

                SettingType.Default -> it.copy()
            }

        }


    }

    fun saveUpdatedSettingsState() {
        viewModelScope.launch {
            saveSettings.invoke(_state.value.toSettings())
        }
    }

    private fun SettingsState.toSettings(): AppSettings {
        val currency = createGlobalSettings(this.currency)
        val capacity = createGlobalSettings(this.capacity)
        val distance = createGlobalSettings(this.distance)
        val avgConsumption = createGlobalSettings(this.avgConsumption)
        val formatOfDate = createGlobalSettings(this.formatOfDate)
        return AppSettings(
            capacity = capacity,
            currency = currency,
            avgConsumption = avgConsumption,
            distance = distance,
            dateFormat = formatOfDate
        )
    }

    private fun createGlobalSettings(item: SettingsStateItem): SettingItem {
        return SettingItem(
            id = item.id,
            name = item.settingsName,
            valueUnit = item.settingsUnitValue,
            type = item.field
        )
    }
}











