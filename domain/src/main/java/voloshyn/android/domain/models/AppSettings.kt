package voloshyn.android.domain.models


data class AppSettings(
    val currency: ItemSetting,
    val distance: ItemSetting,
    val capacity: ItemSetting,
    val avgConsumption: ItemSetting,
    val dateFormat: ItemSetting,
)

data class ItemSetting(
    val id: String = "",
    val name: String = "",
    val valueUnit: String = "",
    val type: SettingType = SettingType.Default
)

typealias AppUnit = String


enum class SettingType {
    Currency, Capacity, Distance, AvgConsumption, FormatOfDate, Default
}










