package voloshyn.android.data.localeStorage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import voloshyn.android.domain.models.AppSettings
import voloshyn.android.domain.models.SettingItem

@Entity(
    tableName = "settings",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["user_id"], unique = true)]
)
data class SettingsEntity(
    @PrimaryKey(autoGenerate = false) val id: Long=1L,
    @ColumnInfo(name = "user_id") val userId: String,
    val currency: String,
    val distance: String,
    val capacity: String,
    @ColumnInfo(name = "avg_consumption") val avgConsumption: String,
    @ColumnInfo(name = "format_of_date") val formatOfDate: String
) {

//    fun toSettings(): Settings {
//        return Settings(
//            unitOfCapacity = Capacity(name = capacity),
//            unitOfAvgConsumption = AvgConsumption(name = avgConsumption),
//            unitOfCurrency = Currency(name = currency),
//            unitOfDistance = Distance(name = distance),
//            dateFormat = FormatOfDate(name = formatOfDate)
//        )
//    }

    companion object {
        fun toEntity(appSettings: AppSettings, userId: String): SettingsEntity {
            return SettingsEntity(
                id = 0,
                userId = userId,
                avgConsumption = concatenateString(appSettings.avgConsumption),
                currency = concatenateString(appSettings.currency),
                capacity = concatenateString(appSettings.capacity),
                distance = concatenateString(appSettings.distance),
                formatOfDate = concatenateString(appSettings.dateFormat)
            )
        }

        private fun concatenateString(value:SettingItem): String {
            return "${value.id}|${value.valueUnit}"
        }
    }
}