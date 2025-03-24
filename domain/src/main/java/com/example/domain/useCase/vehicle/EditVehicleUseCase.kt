package com.example.domain.useCase.vehicle

import com.example.domain.model.Vehicle
import com.example.domain.repository.UserSessionRepository
import com.example.domain.repository.VehiclesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EditVehicleUseCase  @Inject constructor(
    private val repository: VehiclesRepository,
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(newVehicle: Vehicle) {
        repository.editVehicle(
            uUid = userSessionRepository.observeCurrentUserId().first()!!,
            vehicle = newVehicle
        )
    }
}