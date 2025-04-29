package com.example.localedatasource.appPreferencesFromAssets.models

import com.google.gson.annotations.SerializedName

data class PreferencesResponse(
    @SerializedName(value = "avg_consumption")
    val avgConsumptions: List<AvgConsumptionJson>,
    val capacities: List<CapacityJson>,
    val currencies: List<CurrencyJson>,
    @SerializedName(value = "date_formats")
    val dateFormats: List<DateFormatJson>,
    val distances: List<DistanceJson>
)
