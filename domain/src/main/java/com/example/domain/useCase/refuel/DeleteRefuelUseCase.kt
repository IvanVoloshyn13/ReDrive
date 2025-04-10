package com.example.domain.useCase.refuel

import com.example.domain.repository.RefuelRepository
import javax.inject.Inject

class DeleteRefuelUseCase @Inject constructor(
    private val refuelRepository: RefuelRepository,
    ) {
    suspend operator fun invoke(refuelId: Long) = refuelRepository.deleteRefuel(refuelId)
}