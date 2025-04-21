package com.example.domain.useCase

import com.example.domain.model.Refuel
import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.model.log.VehicleWithLogs
import com.example.domain.repository.RefuelRepository
import com.example.domain.repository.UnitPreferencesRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveRefuelLogsUseCase @Inject constructor(
    private val refuelRepository: RefuelRepository,
    private val currentVehicleUseCase: ObserveCurrentVehicleUseCase,
    private val unitPreferencesRepository: UnitPreferencesRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<VehicleWithLogs> {
        return currentVehicleUseCase().flatMapLatest {
            it?.let { vehicle ->
                return@flatMapLatest combine(
                    refuelRepository.observeRefuels(vehicleId = vehicle.id),
                    unitPreferencesRepository.observeUnitPreferences(vehicleId = vehicle.id),
                ) { refuels, settings ->
                    RefuelsWithSettings(refuels, settings)
                }.map { refuelsWithSettings ->
                    val preferences = refuelsWithSettings.unitPreferences
                    val pattern = unitPreferencesRepository.getCurrentDateFormatPattern(vehicle.id)
                    val avgConsumptionTypeKey =
                        unitPreferencesRepository.getAvgConsumptionTypeKey(vehicleId = vehicle.id)
                    val avgConsumptionType = AvgConsumptionType.fromKey(avgConsumptionTypeKey)
                    val logs = refuelsWithSettings.refuels.mapIndexed { i, refuel ->
                        val previousOdometerReading = if (i == 0) vehicle.initialOdometerValue
                        else refuelsWithSettings.refuels[i - 1].odometerValue
                        with(RefuelLogBuilder) {
                            refuel.toRefuelLog(
                                previousOdometerReading,
                                avgConsumptionType,
                                preferences,
                                pattern
                            )
                        }
                    }.sortedByDescending { log ->
                        log.odometerRead
                    }
                    VehicleWithLogs(vehicle, logs)
                }
            } ?: flowOf(
                VehicleWithLogs()
            )
        }
    }

}


internal data class RefuelsWithSettings(
    val refuels: List<Refuel>,
    val unitPreferences: UnitsPreferencesAbbreviation
)



