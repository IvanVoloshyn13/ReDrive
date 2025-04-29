package com.example.localedatasource.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    val id: Long ,
    @ColumnInfo(name = "vehicle_id") val vehicleId: Long,
    val date: Long,
    val odometer: Int,
    @ColumnInfo(name = "fuel_volume") val fuelVolume: Double,
    @ColumnInfo(name = "unit_price") val unitPrice: Double,
    @ColumnInfo(defaultValue = "") val notes: String,
    @ColumnInfo(name = "full_tank") val fullTank: Boolean,
    @ColumnInfo(name = "missed_previous") val missedPrevious: Boolean
)