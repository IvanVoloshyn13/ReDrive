package com.example.domain.repository

import com.example.domain.model.AvgConsumption
import com.example.domain.model.Capacity
import com.example.domain.model.Currency
import com.example.domain.model.DateFormatPattern
import com.example.domain.model.Distance
import com.example.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    /**
     * Retrieves default settings when no user or vehicle is available.
     * Also used as the default settings for new vehicles, which the user can modify later.
     */
    suspend fun getDefaultSettings(): Settings

    /**
     * Observes and returns the settings for the currently selected vehicle.
     */
    fun observeAppSettings(vehicleId: Long): Flow<Settings>
    /**
     * Returns a list of available currency units for the app.
     */
    fun getCurrencyUnits(): List<Currency>
    /**
     * Returns a list of available units for average consumption (e.g., liters per kilometer).
     */
    fun getAvgConsumptionUnits(): List<AvgConsumption>
    /**
     * Returns a list of available units for vehicle capacity (e.g., liters, gallons).
     */
    fun getCapacityUnits(): List<Capacity>
    /**
     * Returns a list of available units for distance (e.g., kilometers, miles).
     */
    fun getDistanceUnits(): List<Distance>
    /**
     * Returns a list of available date format patterns for displaying dates in the app.
     */
    fun getDateFormatPatterns(): List<DateFormatPattern>
    /**
     * Retrieves the currently set date format pattern for the app.
     */
    suspend fun getDateFormatPattern(): String

    /**
     * Updates the app settings for the current vehicle.
     * This allows the settings to be customized for each vehicle.
     */
    suspend fun updateSettings(settings: Settings, vehicleId: Long)

}