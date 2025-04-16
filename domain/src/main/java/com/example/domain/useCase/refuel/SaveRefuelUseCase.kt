package com.example.domain.useCase.refuel

import com.example.domain.RefuelException
import com.example.domain.VehicleException
import com.example.domain.model.Refuel
import com.example.domain.repository.RefuelRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class SaveRefuelUseCase @Inject constructor(
    private val refuelRepository: RefuelRepository,
    private val observeCurrentVehicleUseCase: ObserveCurrentVehicleUseCase
) {
    /**
     * Use case responsible for saving a new Refuel entry to the database.
     *
     * @param refuel The Refuel data to be saved.
     *
     * @throws VehicleException.NoCurrentVehicleException if the user attempts to add refuel data
     * without creating a vehicle first.
     *
     * @throws RefuelException.InvalidOdometerValueException if the provided odometer value is
     * smaller than the initial odometer value of the current vehicle.
     */
    suspend operator fun invoke(refuel: Refuel) {
        val currentVehicle = observeCurrentVehicleUseCase().first()
            ?: throw VehicleException.NoCurrentVehicleException()

        if (refuel.odometerValue < currentVehicle.initialOdometerValue) {
            throw RefuelException.InvalidOdometerValueException(currentVehicle.initialOdometerValue)
        }

        refuelRepository.saveRefuel(refuel = refuel, vehicleId = currentVehicle.id)
    }

}