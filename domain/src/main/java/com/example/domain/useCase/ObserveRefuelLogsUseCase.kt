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
import java.math.RoundingMode
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
                    settingsRepository.observeAppSettings(vehicleId = vehicle.id),
                ) { refuels, settings ->
                    RefuelsWithSettings(refuels, settings)
                }.map { refuelsWithSettings ->
                    var previousOdometerReading: Int
                    settings = refuelsWithSettings.settings
                    val avgConsumptionTypeKey =
                        settingsRepository.getAvgConsumptionType(vehicleId = vehicle.id)
                    val avgConsumptionType = findAvgConsumptionType(avgConsumptionTypeKey)
                    val logs = refuelsWithSettings.refuels.mapIndexed { i, refuel ->
                        if (i == 0) {
                            refuel.toRefuelLog(1, avgConsumptionType)
                        } else {
                            previousOdometerReading =
                                refuelsWithSettings.refuels[i - 1].odometerValue
                            refuel.toRefuelLog(previousOdometerReading, avgConsumptionType)
                        }
                    }
                    VehicleWithLogs(vehicle, logs)
                }
            } ?: flowOf(
                VehicleWithLogs()
            )
        }
    }


    private fun Refuel.toRefuelLog(
        previousOdometerReading: Int,
        avgConsumptionType: AvgConsumptionType
    ): RefuelLog {
        val date = this.refuelDate
        val travelledDistance = this.odometerValue - previousOdometerReading
        val fuelAmount = this.fuelAmount
        val pricePerUnit = this.pricePerUnit
        val payment = pricePerUnit * fuelAmount.formatToScale()
        val avgConsumption = getAvgConsumptionByType(
            avgConsumptionType,
            travelledDistance,
            fuelAmount
        ).formatToScale()
        return RefuelLog(
            id = this.id,
            date = date,
            avgConsumption = avgConsumption.concatenateValueWithUnit(settings.avgConsumption),
            travelledDistance = travelledDistance.concatenateValueWithUnit(settings.distance),
            odometerReading = this.odometerValue.concatenateValueWithUnit(settings.distance),
            fuelAmount = fuelAmount.concatenateValueWithUnit(settings.capacity),
            pricePerUnit = pricePerUnit.concatenateValueWithUnit("${settings.currency}/${settings.capacity}"),
            payment = payment.concatenateValueWithUnit(settings.currency),
        )
    }

    private fun <T : Number> T.concatenateValueWithUnit(unit: String): String {
        return "$this $unit"
    }

    private fun Double.formatToScale(): Double {
        return if (this <= 0) 1.0 else this.toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun findAvgConsumptionType(key: String): AvgConsumptionType {
        return AvgConsumptionType.fromKey(key)
    }

    private fun getAvgConsumptionByType(
        type: AvgConsumptionType,
        travelledDistance: Int,
        fuelAmount: Double
    ): Double {
        return when (type) {
            AvgConsumptionType.LITERS_PER_100KM -> {
                fuelAmount * 100 / travelledDistance
            }

            AvgConsumptionType.KILOMETERS_PER_LITER -> travelledDistance / fuelAmount
            AvgConsumptionType.LITERS_PER_MILE -> fuelAmount / travelledDistance
            AvgConsumptionType.MPG_US -> travelledDistance / fuelAmount
            AvgConsumptionType.MPG_IMP -> travelledDistance / fuelAmount
        }
    }

}


private enum class AvgConsumptionType(val key: String) {
    LITERS_PER_100KM("L_PER_100KM"),
    KILOMETERS_PER_LITER("KM_PER_L"),
    LITERS_PER_MILE("L_PER_MI"),
    MPG_US("MPG_US"),
    MPG_IMP("MPG_IMP");

    companion object {
        fun fromKey(key: String): AvgConsumptionType {
            return entries.find { it.key == key } ?: LITERS_PER_100KM
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