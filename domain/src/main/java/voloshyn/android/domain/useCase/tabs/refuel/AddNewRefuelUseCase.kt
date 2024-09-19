package voloshyn.android.domain.useCase.tabs.refuel

import voloshyn.android.domain.models.tabs.Refuel
import voloshyn.android.domain.repository.tabs.RefuelsRepository

class AddNewRefuelUseCase(private val repository: RefuelsRepository) {

    suspend fun invoke(refuel: Refuel)=repository.addNew(refuel)
}