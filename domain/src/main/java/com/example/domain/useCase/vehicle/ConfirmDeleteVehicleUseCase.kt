package com.example.domain.useCase.vehicle

import com.example.domain.repository.VehiclesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ConfirmDeleteVehicleUseCase @Inject constructor(
    private val repository: VehiclesRepository,
    private val vehiclesUseCase: ObserveVehiclesUseCase
) {
    suspend operator fun invoke(vehicleId: Long) {
        repository.confirmCurrentVehicleDelete(vehicleId)
        setNewCurrentVehicle()
    }

    private suspend fun setNewCurrentVehicle() {
        val vehicles = vehiclesUseCase().first()
        if (vehicles.isEmpty()) return
        val newCurrentVehicle = vehicles.find { !it.isCurrentVehicle }

        newCurrentVehicle?.let { repository.setVehicleAsCurrent(it.id) }
    }
}