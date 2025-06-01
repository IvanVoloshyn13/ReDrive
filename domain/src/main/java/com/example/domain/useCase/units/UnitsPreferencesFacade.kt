package com.example.domain.useCase.units

import com.example.domain.repository.VehicleUnitPreferencesRepository
import javax.inject.Inject

/**
 * A facade that provides a simple interface for accessing independent setting units
 * (e.g., currency units, distance units, etc.) from the repository.
 */
class UnitsPreferencesFacade @Inject constructor(
    private val repository: VehicleUnitPreferencesRepository
) {
    fun getCurrencyUnits() = repository.getCurrencyUnits()
    fun getDistanceUnits() = repository.getDistanceUnits()
    fun getAvgConsumptionUnits() = repository.getAvgConsumptionUnits()
    fun getCapacityUnits() = repository.getCapacityUnits()
    fun getDateFormatPatterns() = repository.getDateFormatPatterns()
}