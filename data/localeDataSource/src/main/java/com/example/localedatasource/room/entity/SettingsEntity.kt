package com.example.localedatasource.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "app_settings",
    foreignKeys = [ForeignKey(
        entity = VehicleEntity::class,
        parentColumns = ["id"],
        childColumns = ["vehicle_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["vehicle_id"], unique = true)]
)
data class SettingsEntity(
    @PrimaryKey(autoGenerate = false) val id: Long = 1L,
    @ColumnInfo(name = "vehicle_id") val vehicleId: String,
    val currency: Currency,
    val distance: Distance,
    val capacity: Capacity,
    @ColumnInfo(name = "avg_consumption") val avgConsumption: AvgConsumption,
    @ColumnInfo(name = "date_format_pattern") val dateFormatPatter: DateFormatPattern
)

data class Currency(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
)

data class Distance(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
)

data class Capacity(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
)

data class AvgConsumption(
    val id: Int = 0,
    val unit: String = "",
    val abbreviation: String = "",
)

data class DateFormatPattern(
    val id: Int = 0,
    val pattern: String = ""
)