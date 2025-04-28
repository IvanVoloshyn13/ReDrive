package com.example.domain.useCase.overview

import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.model.Vehicle
import com.example.domain.model.log.LogPreferences
import com.example.domain.model.log.RefuelLog
import com.example.domain.repository.OverviewRepository
import com.example.domain.repository.UnitPreferencesRepository
import com.example.domain.useCase.logs.AvgConsumptionType
import com.example.domain.useCase.logs.RefuelLogBuilder.toRefuelLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for observing the last refuel log for a specific vehicle.
 *
 * This use case listens for changes to the last refuel and the previous odometer value,
 * maps them into a [RefuelLog] format, and emits updated logs when relevant data changes.
 *
 * It retrieves user-specific unit preferences (like distance, volume, and average consumption types)
 * to properly format the refuel log for UI presentation or further processing.
 *
 * @param repository Repository providing access to overview and refuel data.
 * @param unitPreferencesRepository Repository providing access to user's unit and formatting preferences.
 */
class ObserveLastRefuelLogUseCase @Inject constructor(
    private val repository: OverviewRepository,
    private val unitPreferencesRepository: UnitPreferencesRepository
) {
    fun invoke(vehicle: Vehicle, preferences: UnitsPreferencesAbbreviation): Flow<RefuelLog?> {
        return repository.observeLastRefuel(vehicleId = vehicle.id).distinctUntilChanged()
            .map { refuel ->
                val odometerReading = repository.fetchSecondLastOdometerReading(vehicle.id)
                val pref = getLogPreferences(vehicle.id)
                refuel?.toRefuelLog(
                    previousOdometerReading = odometerReading
                        ?: vehicle.initialOdometerValue,
                    avgConsumptionType = pref.avgConsumptionType,
                    unitPreferences = preferences,
                    pattern = pref.datePattern
                )
            }
    }

    private suspend fun getLogPreferences(vehicleId: Long): LogPreferences {
        val pattern =
            unitPreferencesRepository.getCurrentDateFormatPattern(vehicleId)
        val avgConsumptionTypeKey =
            unitPreferencesRepository.getAvgConsumptionTypeKey(vehicleId = vehicleId)
        val avgConsumptionType = AvgConsumptionType.fromKey(avgConsumptionTypeKey)
        return LogPreferences(datePattern = pattern, avgConsumptionType = avgConsumptionType)
    }

}