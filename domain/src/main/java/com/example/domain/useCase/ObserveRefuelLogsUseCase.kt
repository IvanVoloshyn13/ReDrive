package com.example.domain.useCase

import com.example.domain.model.Refuel
import com.example.domain.model.RefuelLog
import com.example.domain.model.Settings
import com.example.domain.model.Vehicle
import com.example.domain.repository.RefuelRepository
import com.example.domain.repository.SettingsRepository
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
    private val settingsRepository: SettingsRepository
) {

    private lateinit var settings: Settings

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<VehicleWithLogs> {
        return currentVehicleUseCase().flatMapLatest {
            it?.let { vehicle ->
                return@flatMapLatest combine(
                    refuelRepository.observeRefuels(vehicleId = vehicle.id),
                    settingsRepository.observeAppSettings(vehicleId = vehicle.id)
                ) { refuels, settings ->
                    RefuelsWithSettings(refuels, settings)
                }.map { refuelsWithSettings ->
                    settings = refuelsWithSettings.settings
                    val avgConsumptionType = findAvgConsumptionType()
                    val logs = refuelsWithSettings.refuels.mapIndexed { i, refuel ->
                        if (i == 0) {
                            refuel.initialLog(vehicle)
                        } else {
                            refuel.toLog()
                        }

                    }
                    VehicleWithLogs(vehicle, logs)
                }
            } ?: flowOf(
                VehicleWithLogs()
            )
        }
    }


    private fun Refuel.toLog(): RefuelLog {
        TODO()
    }

    private fun Refuel.initialLog(vehicle: Vehicle): RefuelLog {
        val date = this.refuelDate
        val travelledDistance =
            (this.odometerValue - vehicle.initialOdometerValue).toString() + " " + settings.distance
        val fuelAmount = this.fuelVolume.toString() + " " + settings.capacity
        val pricePerUnit =
            this.pricePerUnit.toString() + " " + "${settings.currency}/${settings.capacity}"
        val payment = (this.pricePerUnit * this.fuelVolume).toString() + " " + settings.currency
        val avgConsumption = toAvgConsumption(TODO())

        TODO()
    }

    private fun findAvgConsumptionType(): AvgConsumptionType {
        TODO()
    }

    private fun toAvgConsumption(type: AvgConsumptionType): String {
        TODO()
    }

}


private enum class AvgConsumptionType(val abbreviation: String) {
    LITERS_PER_100KM("l/100km"),
    KILOMETERS_PER_LITER("km/l"),
    LITERS_PER_MILE("l/mi"),
    MPG_US("mpg (US)"),
    MPG_IMP("mpg (imp)"),
    MILES_PER_GALLON_US("mi/gal (US)"),
    MILES_PER_GALLON_IMP("mi/gal (imp)");

    companion object {
        fun fromAbbreviation(abbreviation: String): AvgConsumptionType {
            return entries.find { it.abbreviation == abbreviation } ?: LITERS_PER_100KM
        }
    }
}

private data class RefuelsWithSettings(
    val refuels: List<Refuel>,
    val settings: Settings
)

data class VehicleWithLogs(
    val vehicle: Vehicle? = null,
    val logs: List<RefuelLog> = emptyList()
)