package com.example.localedatasource.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class RoomTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromAvgConsumptionToString(item: AvgConsumption): String {
        return gson.toJson(item)
    }

    @TypeConverter
    fun toAvgConsumption(json: String): AvgConsumption {
        val type = object : TypeToken<AvgConsumption>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromCapacityToString(capacity: Capacity): String {
        return gson.toJson(capacity)
    }

    @TypeConverter
    fun toCapacity(json: String): Capacity {
        val type = object : TypeToken<Capacity>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromCurrencyToString(currency: Currency): String {
        return gson.toJson(currency)
    }

    @TypeConverter
    fun toCurrency(json: String): Currency {
        val type = object : TypeToken<Currency>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromDistanceToString(distance: Distance): String {
        return gson.toJson(distance)
    }

    @TypeConverter
    fun toDistance(json: String): Distance {
        val type = object : TypeToken<Distance>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromDateFormatToString(dateFormat: DateFormatPattern): String {
        return gson.toJson(dateFormat)
    }

    @TypeConverter
    fun toDateFormat(json: String): DateFormatPattern {
        val type = object : TypeToken<DateFormatPattern>() {}.type
        return gson.fromJson(json, type)
    }
}

data class Currency(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
)

data class Distance(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
)

data class Capacity(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
)

data class AvgConsumption(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
)

data class DateFormatPattern(
    val id: Int = 0,
    val pattern: String = ""
)