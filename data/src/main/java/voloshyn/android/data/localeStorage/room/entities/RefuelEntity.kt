package voloshyn.android.data.localeStorage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import voloshyn.android.domain.models.refuel.Refuel

@Entity(
    tableName = "refuels",
    foreignKeys = [ForeignKey(
        entity = VehicleEntity::class,
        parentColumns = ["id"],
        childColumns = ["vehicle_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["vehicle_id"])]
)
data class RefuelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "vehicle_id") val vehicleId: Long,
    val date: Long,
    val odometer: Int,
    @ColumnInfo(name = "fuel_volume") val fuelVolume: Double,
    @ColumnInfo(name = "unit_price") val unitPrice: Double,
    @ColumnInfo(defaultValue = "") val notes: String,
    @ColumnInfo(name = "full_tank") val fullTank: Boolean,
    @ColumnInfo(name = "missed_previous") val missedPrevious: Boolean
) {

    fun toRefuel(): Refuel {
        return Refuel(
            id=id,
            date = date,
            odometer = odometer,
            fuelVolume = fuelVolume,
            unitPrice = unitPrice,
            notes = notes,
            fullTank = fullTank,
            missedPrevious = missedPrevious
        )
    }


    companion object {
        fun toEntity(refuel: Refuel,vehicleId: Long): RefuelEntity {
            with(refuel) {
                return RefuelEntity(
                    vehicleId = vehicleId,
                    date =date, //date ,
                    odometer = odometer,
                    fuelVolume = fuelVolume,
                    unitPrice = unitPrice,
                    notes = notes,
                    fullTank = fullTank,
                    missedPrevious = missedPrevious

                )
            }
        }
    }
}