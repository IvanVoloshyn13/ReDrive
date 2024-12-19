package voloshyn.android.domain.useCase.logs

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import voloshyn.android.domain.models.logs.Logs
import voloshyn.android.domain.models.logs.RefuelLog
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.refuel.Refuel
import voloshyn.android.domain.repository.RefuelLogsRepository

import java.math.RoundingMode


class GetLogsUseCase(
    private val repository: RefuelLogsRepository,

    ) {
    fun invoke(): Flow<Logs> {
        val vehicleWithRefuels = repository.getVehicleWithRefuels()
        vehicleWithRefuels.map {
            toRefuelLogs(it.vehicle,it.refuelLogs)
        }
        TODO()
    }

    private fun toRefuelLogs(vehicle: Vehicle, refuels: List<Refuel>): List<RefuelLog> {
        val refuelLogs = ArrayList<RefuelLog>()
        //Initial odometer value which will be change for each iteration
        var previousOdometerVal = vehicle.initialOdometerValue

        refuels.forEach { refuel ->
            val travelledDistance = refuel.odometer - previousOdometerVal
            val avgFuelConsumption = refuel.fuelVolume * 100 / travelledDistance
            val fuelVolume = refuel.fuelVolume
            val unitPrice = refuel.unitPrice
            val payments = fuelVolume * unitPrice

            // Create a new RefuelLog object
            val refuelLog = RefuelLog(
                id = refuel.id,
                odometer = refuel.odometer.toString(),
                avgFuelConsumption = avgFuelConsumption.formatToScale(),
                travelledDistance = travelledDistance.toString(),
                fuelVolume = refuel.fuelVolume.formatToScale(),
                payments = payments.formatToScale(),
                unitPrice = refuel.unitPrice.formatToScale(),
                notes = refuel.notes
            )
            refuelLogs.add(refuelLog)
            previousOdometerVal = refuel.odometer
        }
        return refuelLogs
    }

    private fun Double.formatToScale(): String {
        return this.toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP).toString()
    }
}