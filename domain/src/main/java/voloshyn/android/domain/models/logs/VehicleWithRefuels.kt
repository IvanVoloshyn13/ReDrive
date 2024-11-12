package voloshyn.android.domain.models.logs

import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.refuel.Refuel

data class VehicleWithRefuels(
    val vehicle: Vehicle,
    val refuelLogs: List<Refuel>,
    val units: RefuelUnits
    //TODO() repair logs
)


data class RefuelUnits(
    val odometer: OdometerUnit,
    val payment: PaymentUnit,
    val fuelVolume: FuelVolumeUnit
)

@JvmInline
value class OdometerUnit(val odometer:String)

@JvmInline
value class PaymentUnit(val payment:String)

@JvmInline
value class FuelVolumeUnit(val fuelVolume:String)