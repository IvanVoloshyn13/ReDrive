package com.example.localedatasource.inMemoryAppSettings.models

import com.google.gson.annotations.SerializedName

data class DefaultAppSettingsResponse(
    @SerializedName(value = "avg_consumption")
    val avgConsumption: AvgConsumptionJson,
    val capacity: CapacityJson,
    val currency: CurrencyJson,
    @SerializedName(value = "date_format")
    val dateFormat: DateFormatJson,
    val distance: DistanceJson
)
