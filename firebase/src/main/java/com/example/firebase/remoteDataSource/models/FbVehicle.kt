package com.example.firebase.remoteDataSource.models

data class FbVehicle(
    val id: Long = 0,
    val userId:String,
    val name: String,
    val initialOdometerValue: Int,
    val type: String,
    val isCurrentVehicle: Boolean
)