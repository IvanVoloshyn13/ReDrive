package com.example.domain.useCase.refuel

import com.example.domain.VehicleException
import com.example.domain.model.Refuel
import com.example.domain.repository.RefuelRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateRefuelUseCase @Inject constructor(
    private val refuelRepository: RefuelRepository,
    private val observeCurrentVehicleUseCase: ObserveCurrentVehicleUseCase
) {
    suspend operator fun invoke(refuel: Refuel) {
        val currentVehicle = observeCurrentVehicleUseCase().first()!!
        if (refuel.odometerValue < currentVehicle.initialOdometerValue) {
            throw VehicleException.InvalidOdometerValueException(currentVehicle.initialOdometerValue)
        }

        refuelRepository.updateRefuel(refuel, currentVehicle.id)
    }
}