package com.example.domain.repository

import com.example.domain.model.AvgConsumption
import com.example.domain.model.Capacity
import com.example.domain.model.Currency
import com.example.domain.model.DateFormatPattern
import com.example.domain.model.Distance
import com.example.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getDefaultSettings(): Settings
    fun observeAppSettings(vehicleId: Long): Flow<Settings>
    fun getCurrencyUnits(): List<Currency>
    fun getAvgConsumptionUnits(): List<AvgConsumption>
    fun getCapacityUnits(): List<Capacity>
    fun getDistanceUnits(): List<Distance>
    fun getDateFormatPatterns(): List<DateFormatPattern>
    suspend fun updateSettings(settings: Settings,vehicleId: Long)
    suspend fun getDateFormatPattern(): String
}