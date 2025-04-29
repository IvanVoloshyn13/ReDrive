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
data class UnitPreferencesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "vehicle_id") val vehicleId: Long = 0,
    val currencyKey: String,
    val distanceKey: String,
    val capacityKey: String,
    @ColumnInfo(name = "avg_consumption_key") val avgConsumptionKey: String,
    @ColumnInfo(name = "date_format_pattern_key") val dateFormatPatternKey: String
)

