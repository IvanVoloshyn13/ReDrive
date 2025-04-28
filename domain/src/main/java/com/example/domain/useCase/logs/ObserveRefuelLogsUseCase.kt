package com.example.domain.useCase.logs

import com.example.domain.model.Refuel
import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.model.Vehicle
import com.example.domain.model.log.LogPreferences
import com.example.domain.model.log.VehicleWithLogs
import com.example.domain.repository.RefuelRepository
import com.example.domain.repository.UnitPreferencesRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case that observes the current vehicle and emits its full refuel log history,
 * formatted according to the user’s unit and date preferences.
 *
 * - Listens for changes to the “current” vehicle.
 * - For a non-null vehicle, combines its refuel entries with unit settings,
 *   builds a chronological list of [RefuelLog] entries, and wraps them in [VehicleWithLogs].
 * - If no vehicle is selected, emits an empty [VehicleWithLogs].
 *
 * @property refuelRepository Provides access to stored refuels.
 * @property currentVehicleUseCase Emits the currently selected [Vehicle].
 * @property unitPreferencesRepository Provides unit and date formatting preferences.
 */
class ObserveRefuelLogsUseCase @Inject constructor(
    private val refuelRepository: RefuelRepository,
    private val currentVehicleUseCase: ObserveCurrentVehicleUseCase,
    private val unitPreferencesRepository: UnitPreferencesRepository
) {

    /**
     * Starts observing the selected vehicle and its refuel logs.
     *
     * Whenever the current vehicle changes:
     *  - If it’s non-null, streams a [VehicleWithLogs] built via [makeVehicleWithLogs].
     *  - If it’s null, emits a default, empty [VehicleWithLogs].
     *
     * @return A [Flow] of [VehicleWithLogs], updating on vehicle switch or refuel/unit-preference changes.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<VehicleWithLogs> {
        return currentVehicleUseCase()
            .distinctUntilChanged()
            .flatMapLatest { vehicle ->
                vehicle?.let(::makeVehicleWithLogs)
                    ?: flowOf(
                        VehicleWithLogs()
                    )
            }
    }

    /**
     * Combines the list of all refuels for [vehicle] with its current unit preferences,
     * then transforms into a [VehicleWithLogs].
     *
     * @param vehicle The vehicle whose refuels to observe.
     * @return A [Flow] emitting [VehicleWithLogs] whenever refuels or unit settings change.
     */
    private fun makeVehicleWithLogs(vehicle: Vehicle): Flow<VehicleWithLogs> =
        combine(
            refuelRepository.observeRefuels(vehicleId = vehicle.id),
            unitPreferencesRepository.observeUnitPreferences(vehicleId = vehicle.id),
        ) { refuels, pref ->
            RefuelsAndPrefs(refuels, pref)
        }.map { refuelsWithSettings ->
            createVehicleWithLogs(vehicle, refuelsWithSettings)
        }

    /**
     * Builds the [VehicleWithLogs] by:
     * 1. Fetching the user’s [LogPreferences] (date format & avg consumption).
     * 2. Mapping each [Refuel] to a log entry, supplying the previous odometer reading.
     * 3. Sorting logs descending by odometer to show newest first.
     *
     * @param vehicle The vehicle for which to build logs.
     * @param refuelsAndPrefs Container holding raw refuels and unit prefs.
     * @return A fully populated [VehicleWithLogs] instance.
     */
    private suspend fun createVehicleWithLogs(
        vehicle: Vehicle,
        refuelsAndPrefs: RefuelsAndPrefs
    ): VehicleWithLogs {
        val logPreferences = fetchLogPreferences(vehicle.id)
        val logs = refuelsAndPrefs.refuels
            .mapIndexed { i, refuel ->
                val previousOdometerReading = if (i == 0) vehicle.initialOdometerValue
                else refuelsAndPrefs.refuels[i - 1].odometerValue
                with(RefuelLogBuilder) {
                    refuel.toRefuelLog(
                        previousOdometerReading,
                        logPreferences.avgConsumptionType,
                        refuelsAndPrefs.unitPreferences,
                        logPreferences.datePattern
                    )
                }
            }.sortedByDescending { log ->
                log.odometerRead
            }
        return VehicleWithLogs(vehicle, logs)
    }

    /**
     * Loads the user’s preferred date pattern and average consumption type for [vehicleId].
     *
     * @param vehicleId ID of the vehicle whose preferences to load.
     * @return A [LogPreferences] containing date formatting and consumption display settings.
     */
     private suspend fun fetchLogPreferences(vehicleId: Long): LogPreferences {
        val pattern =
            unitPreferencesRepository.getCurrentDateFormatPattern(vehicleId)
        val avgConsumptionTypeKey =
            unitPreferencesRepository.getAvgConsumptionTypeKey(vehicleId = vehicleId)
        val avgConsumptionType = AvgConsumptionType.fromKey(avgConsumptionTypeKey)
        return LogPreferences(datePattern = pattern, avgConsumptionType = avgConsumptionType)
    }
}

private data class RefuelsAndPrefs(
    val refuels: List<Refuel>,
    val unitPreferences: UnitsPreferencesAbbreviation
)



