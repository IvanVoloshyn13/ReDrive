package com.example.redrive.presentation.vehicle.vehicles

import com.example.domain.model.Vehicle

data class VehiclesFragmentState(
    val vehicles: List<Vehicle> = ArrayList<Vehicle>(),
    val error: Boolean = false,
    val errorMessage: String = ""
)