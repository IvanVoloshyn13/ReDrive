package com.example.domain.useCase

import com.example.domain.model.Refuel
import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.model.log.RefuelLog
import com.example.domain.model.log.ValueWithUnit
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

object RefuelLogBuilder {
    fun Refuel.toRefuelLog(
        previousOdometerReading: Int,
        avgConsumptionType: AvgConsumptionType,
        unitPreferences: UnitsPreferencesAbbreviation,
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
            date = this.refuelTimeStamp.toFormatedDate(pattern),
            avgConsumption = ValueWithUnit(
                avgConsumption.toString(),
                unitPreferences.avgConsumption
            ),
            travelledDistance = travelledDistance.concatenateValueWithUnit(unitPreferences.distance),
            odometerReading = this.odometerValue.format()
                .concatenateValueWithUnit(unitPreferences.distance),
            odometerRead = this.odometerValue,
            fuelAmount = fuelAmount.concatenateValueWithUnit(unitPreferences.capacity),
            pricePerUnit = pricePerUnit.concatenateValueWithUnit("${unitPreferences.currency}/${unitPreferences.capacity}"),
            payment = payment.concatenateValueWithUnit(unitPreferences.currency),
        )
    }

    private fun Int.format(): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(this).replace(",", " ")
    }

    private fun <T> T.concatenateValueWithUnit(unit: String): String {
        return "$this $unit"
    }

    fun Double.formatToScale(): Double {
        return if (this <= 0) 1.0 else
            this.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    fun getAvgConsumptionByType(
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

    private fun Long.toFormatedDate(pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(this)

    }
}

enum class AvgConsumptionType(val key: String) {
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
