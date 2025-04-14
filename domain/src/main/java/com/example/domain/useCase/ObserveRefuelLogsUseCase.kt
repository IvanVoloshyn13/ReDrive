package com.example.domain.useCase

import android.annotation.SuppressLint
import com.example.domain.model.Refuel
import com.example.domain.model.RefuelLog
import com.example.domain.model.Settings
import com.example.domain.model.VehicleWithLogs
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
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ObserveRefuelLogsUseCase @Inject constructor(
    private val refuelRepository: RefuelRepository,
    private val currentVehicleUseCase: ObserveCurrentVehicleUseCase,
    private val settingsRepository: SettingsRepository
) {

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
                    val settings = refuelsWithSettings.settings
                    val pattern = settingsRepository.getDateFormatPattern(vehicle.id)
                    val avgConsumptionTypeKey =
                        settingsRepository.getAvgConsumptionType(vehicleId = vehicle.id)
                    val avgConsumptionType = AvgConsumptionType.fromKey(avgConsumptionTypeKey)
                    val logs = refuelsWithSettings.refuels.mapIndexed { i, refuel ->
                        val previousOdometerReading = if (i == 0) vehicle.initialOdometerValue
                        else refuelsWithSettings.refuels[i - 1].odometerValue

                        refuel.toRefuelLog(
                            previousOdometerReading,
                            avgConsumptionType,
                            settings,
                            pattern
                        )

                    }.sortedByDescending { log ->
                        log.odometerReading
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
        avgConsumptionType: AvgConsumptionType,
        settings: Settings,
        pattern: String
    ): RefuelLog {
        val travelledDistance = (this.odometerValue - previousOdometerReading)
        val fuelAmount = this.fuelAmount
        val pricePerUnit = this.pricePerUnit
        val payment = (pricePerUnit * fuelAmount).formatToScale()
        val avgConsumption = getAvgConsumptionByType(
            avgConsumptionType,
            travelledDistance,
            fuelAmount
        ).formatToScale()
        return RefuelLog(
            id = this.id,
            date = this.refuelDate.toFormatedDate(pattern),
            avgConsumption = Pair(avgConsumption.toString(), settings.avgConsumption),
            travelledDistance = travelledDistance.concatenateValueWithUnit(settings.distance),
            odometerReading = this.odometerValue.format()
                .concatenateValueWithUnit(settings.distance),
            fuelAmount = fuelAmount.concatenateValueWithUnit(settings.capacity),
            pricePerUnit = pricePerUnit.concatenateValueWithUnit("${settings.currency}/${settings.capacity}"),
            payment = payment.concatenateValueWithUnit(settings.currency),
        )
    }

    private fun Int.format(): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(this).replace(",", " ")
    }

    private fun <T> T.concatenateValueWithUnit(unit: String): String {
        return "$this $unit"
    }

    private fun Double.formatToScale(): Double {
        return if (this <= 0) 1.0 else
            this.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun getAvgConsumptionByType(
        type: AvgConsumptionType,
        travelledDistance: Int,
        fuelAmount: Double
    ): Double {
        return when (type) {
            AvgConsumptionType.LITERS_PER_100KM -> (fuelAmount * 100) / travelledDistance
            AvgConsumptionType.KILOMETERS_PER_LITER -> travelledDistance / fuelAmount
            AvgConsumptionType.LITERS_PER_MILE -> fuelAmount / travelledDistance
            AvgConsumptionType.MPG_US, AvgConsumptionType.MPG_IMP -> travelledDistance / fuelAmount
        }
    }
}

private fun Long.toFormatedDate(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)

}

internal enum class AvgConsumptionType(val key: String) {
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

internal data class RefuelsWithSettings(
    val refuels: List<Refuel>,
    val settings: Settings
)



