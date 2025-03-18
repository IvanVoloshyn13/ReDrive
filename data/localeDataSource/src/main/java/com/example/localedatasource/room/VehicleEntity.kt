package com.example.localedatasource.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehicles",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"], childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["user_id"])]
)
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "user_id") val userId: String,
    val name: String,
    @ColumnInfo(name = "initial_odometer_value") val initialOdometerValue: Int,
    @ColumnInfo(name = "vehicle_type") val vehicleType: String
)
