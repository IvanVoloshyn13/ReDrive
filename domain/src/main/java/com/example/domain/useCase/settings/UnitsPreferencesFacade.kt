package com.example.domain.useCase.settings

import com.example.domain.repository.UnitPreferencesRepository
import javax.inject.Inject

/**
 * A facade that provides a simple interface for accessing independent setting units
 * (e.g., currency units, distance units, etc.) from the repository.
 */
class UnitsPreferencesFacade @Inject constructor(
    private val repository: UnitPreferencesRepository
) {
    fun getCurrencyUnits() = repository.getCurrencies()
    fun getDistanceUnits() = repository.getDistances()
    fun getAvgConsumptionUnits() = repository.getAvgConsumptions()
    fun getCapacityUnits() = repository.getCapacities()
    fun getDateFormatPatterns() = repository.getDateFormatPatterns()
}