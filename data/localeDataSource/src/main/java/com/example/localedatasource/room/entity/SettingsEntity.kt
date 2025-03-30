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
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "vehicle_id") val vehicleId: Long = 0,
    val currency: String,
    val distance: String,
    val capacity: String,
    @ColumnInfo(name = "avg_consumption") val avgConsumption: String,
    @ColumnInfo(name = "date_format_pattern") val dateFormatPatter: String
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