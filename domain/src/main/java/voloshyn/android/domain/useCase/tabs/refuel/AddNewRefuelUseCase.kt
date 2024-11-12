package voloshyn.android.domain.useCase.tabs.refuel

import voloshyn.android.domain.models.refuel.Refuel
import voloshyn.android.domain.repository.tabs.RefuelRepository


/**
 * Use case for adding a new refuel record.
 *
 * @property repository The repository handling refuel operations.
 */
class AddNewRefuelUseCase(private val repository: RefuelRepository) {

    /**
     * Adds a new refuel entry to the repository.
     *
     * @param refuel The refuel data to be added.
     */
    suspend fun invoke(refuel: Refuel) = repository.addNew(refuel)
}