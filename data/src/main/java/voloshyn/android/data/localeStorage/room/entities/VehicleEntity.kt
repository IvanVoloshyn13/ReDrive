package voloshyn.android.data.localeStorage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.VehicleType

@Entity(
    tableName = "vehicles",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id"])]
)
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "user_id") val userId: String,
    val name: String,
    @ColumnInfo(name = "initial_odometer_value") val initialOdometerValue: Int,
    @ColumnInfo(name = "vehicle_type") val vehicleType: String
) {
    fun toVehicle(): Vehicle {
        return Vehicle(
            id = id,
            name = name,
            initialOdometerValue = initialOdometerValue,
            type = VehicleType.valueOf(vehicleType)
        )
    }


    companion object {
        val emptyVehicle = VehicleEntity(
            id = 0,
            userId = "",
            name = "",
            initialOdometerValue = 0,
            vehicleType = VehicleType.Car.name
        )
    }
}
