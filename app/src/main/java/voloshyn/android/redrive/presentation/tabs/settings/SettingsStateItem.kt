package voloshyn.android.redrive.presentation.tabs.settings

import androidx.annotation.DrawableRes
import voloshyn.android.app.R
import voloshyn.android.domain.models.SettingType
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters


data class SettingsStateItem(
    val id: String = "",
    val field: SettingType = SettingType.Default,
    val settingsName: String = "",
    val settingsUnitValue: String = "",
    @DrawableRes val icon: Int = R.drawable.ic_logo
)

data class SettingsState(
    val currency: SettingsStateItem = SettingsStateItem(),
    val capacity: SettingsStateItem = SettingsStateItem(),
    val distance: SettingsStateItem = SettingsStateItem(),
    val avgConsumption: SettingsStateItem = SettingsStateItem(),
    val formatOfDate: SettingsStateItem = SettingsStateItem(),
) {
    fun getComponent(index: Int): SettingsStateItem {
        val maxIndex = SettingsState::class.primaryConstructor?.valueParameters?.size ?: 0
        if (index > maxIndex) throw IllegalArgumentException()
        return when (index) {
            0 -> currency
            1 -> capacity
            2 -> distance
            3 -> avgConsumption
            4 -> formatOfDate
            else -> throw IllegalArgumentException()
        }
    }
}












