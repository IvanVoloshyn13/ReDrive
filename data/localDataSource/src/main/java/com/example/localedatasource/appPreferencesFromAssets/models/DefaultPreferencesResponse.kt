package com.example.localedatasource.appPreferencesFromAssets.models

import com.google.gson.annotations.SerializedName

data class DefaultPreferencesResponse(
    @SerializedName(value = "avg_consumption")
    val avgConsumption: AvgConsumptionJson,
    val capacity: CapacityJson,
    val currency: CurrencyJson,
    @SerializedName(value = "date_format")
    val dateFormat: DateFormatJson,
    val distance: DistanceJson
)
