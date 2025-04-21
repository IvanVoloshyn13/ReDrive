package com.example.domain.useCase.overview

import com.example.domain.model.Refuel
import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.model.Vehicle
import com.example.domain.model.log.RefuelLog
import com.example.domain.repository.OverviewRepository
import com.example.domain.repository.UnitPreferencesRepository
import com.example.domain.useCase.AvgConsumptionType
import com.example.domain.useCase.RefuelLogBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveLastRefuelLogUseCase @Inject constructor(
    private val repository: OverviewRepository,
    private val unitPreferencesRepository: UnitPreferencesRepository
) {
    fun invoke(vehicle: Vehicle,preferences:UnitsPreferencesAbbreviation): Flow<RefuelLog?> {
        return combine(
            repository.observeLastRefuelWithPreviousOdometerReading(vehicle.id),
            unitPreferencesRepository.observeUnitPreferences(vehicle.id)
        ) { pairRefuelWithPrevOdoRead, pref ->
            RefuelLogWithPref(pairRefuelWithPrevOdoRead, pref)
        }.map { refWithPref ->
            val pattern = unitPreferencesRepository.getCurrentDateFormatPattern(vehicle.id)
            val avgConsumptionTypeKey =
                unitPreferencesRepository.getAvgConsumptionTypeKey(vehicleId = vehicle.id)
            val avgConsumptionType = AvgConsumptionType.fromKey(avgConsumptionTypeKey)
            with(RefuelLogBuilder) {
                val refuel = refWithPref.pairRefuelWithPrevOdoRead.first
                refuel?.toRefuelLog(
                    previousOdometerReading = refWithPref.pairRefuelWithPrevOdoRead.second
                        ?: vehicle.initialOdometerValue,
                    avgConsumptionType = avgConsumptionType,
                    unitPreferences = preferences,
                    pattern = pattern
                )
            }
        }.distinctUntilChanged()
    }

    private data class RefuelLogWithPref(
        val pairRefuelWithPrevOdoRead: Pair<Refuel?, Int?>,
        val pref: UnitsPreferencesAbbreviation
    )

}