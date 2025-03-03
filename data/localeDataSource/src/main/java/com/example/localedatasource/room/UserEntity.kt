package com.example.localedatasource.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index("email", unique = true)]
)
data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "email", collate = ColumnInfo.NOCASE)
    val email: String,
    @ColumnInfo(name = "full_name") val fullName: String
)