package voloshyn.android.domain.models

data class AppSettings(
    val currency: SettingItem,
    val distance: SettingItem,
    val capacity: SettingItem,
    val avgConsumption: SettingItem,
    val dateFormat: SettingItem,

    )

data class SettingItem(
    val id: String = "",
    val name: String = "",
    val valueUnit: String = "",
    val type: SettingType = SettingType.Default
)

typealias AppUnit = String


enum class SettingType {
    Currency, Capacity, Distance, AvgConsumption, FormatOfDate, Default
}










