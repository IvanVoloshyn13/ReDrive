package com.example.domain.model

/**
 * Represents the selected abbreviations for each setting category,
 * such as currency, distance, capacity, average consumption, and date format.
 */
data class UnitsPreferencesAbbreviation(
    val id: Long = 0,
    val currency: String = "",
    val distance: String = "",
    val capacity: String = "",
    val avgConsumption: String = "",
    val dateFormatPattern: String = ""
)


data class Currency(
    val id: Int = 0,
    val key: String = "",
    val displayName: String = "",
    val abbreviation: String = "",
) : AppSettingsItem

data class Distance(
    val id: Int = 0,
    val key: String = "",
    val displayName: String = "",
    val abbreviation: String = "",
) : AppSettingsItem

data class Capacity(
    val id: Int = 0,
    val key: String = "",
    val displayName: String = "",
    val abbreviation: String = "",
) : AppSettingsItem

data class AvgConsumption(
    val id: Int = 0,
    val abbreviation: String = "",
    val key: String = "",
) : AppSettingsItem

data class DateFormatPattern(
    val id: Int = 0,
    val key: String = "",
    val pattern: String = "",
    val displayName: String = ""
) : AppSettingsItem

enum class SettingType {
    Currency, Capacity, Distance, AvgConsumption, FormatOfDate
}

interface AppSettingsItem

//Maybe in feature set units dynamically
//fun isValidAvgConsumptionType(
//    distanceUnit: String,
//    capacityUnit: String,
//    avgConsumptionType: AvgConsumptionType
//): Boolean {
//    return when {
//        distanceUnit == "km" && capacityUnit == "l" -> avgConsumptionType in listOf(
//            AvgConsumptionType.L_PER_100KM,
//            AvgConsumptionType.KM_PER_L
//        )
//        distanceUnit == "mi" && capacityUnit == "gal (US)" -> avgConsumptionType in listOf(
//            AvgConsumptionType.MPG_US,
//            AvgConsumptionType.MILES_PER_GALLON_US
//        )
//        distanceUnit == "mi" && capacityUnit == "gal (imp)" -> avgConsumptionType in listOf(
//            AvgConsumptionType.MPG_IMP,
//            AvgConsumptionType.MILES_PER_GALLON_IMP
//        )
//        else -> false
//    }
//}
