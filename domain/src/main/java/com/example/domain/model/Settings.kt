package com.example.domain.model

data class Settings(
    val id: Long = 0,
    val currencyAbbr: String = "",
    val distanceAbbr: String = "",
    val capacityAbbr: String = "",
    val avgConsumptionAbbr: String = "",
    val dateFormatPattern: String = ""
)


data class Currency(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
) : AppSettingsItem

data class Distance(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
) : AppSettingsItem

data class Capacity(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
) : AppSettingsItem

data class AvgConsumption(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
) : AppSettingsItem

data class DateFormatPattern(
    val id: Int = 0,
    val pattern: String = ""
) : AppSettingsItem

enum class SettingType {
    Currency, Capacity, Distance, AvgConsumption, FormatOfDate
}

interface AppSettingsItem















