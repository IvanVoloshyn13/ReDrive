package com.example.domain.useCase.refuel

import com.example.domain.model.Refuel
import com.example.domain.repository.RefuelRepository
import javax.inject.Inject

class GetRefuelByIdUseCase @Inject constructor(
    private val repository: RefuelRepository
) {

    suspend operator fun invoke(refuelId: Long): Refuel {
        return repository.getRefuelById(refuelId)
    }
}