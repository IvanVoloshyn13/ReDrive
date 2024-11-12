package voloshyn.android.data.localeStorage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import voloshyn.android.domain.models.Vehicle

@Entity(
    tableName = "vehicles",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["account_id"])]
)
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "account_id") val accountId: String? = null,
    val name: String = "",
    @ColumnInfo(name = "current_mileage") val currentMileage: Int = 0
) {
    fun toVehicle(): Vehicle {
        return Vehicle(
            id = id,
            name = name,
            currentMileage = currentMileage
        )
    }
}
