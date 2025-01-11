package voloshyn.android.domain.useCase.vehicle

import kotlinx.coroutines.flow.first
import voloshyn.android.domain.repository.VehiclesRepository
import voloshyn.android.domain.repository.userAuth.UserSessionRepository

class IsVehicleUseCase(
    private val repository: VehiclesRepository,
    private val userSessionRepository: UserSessionRepository
) {
    suspend fun invoke(): Boolean {
        val uuid =
            userSessionRepository.observeCurrentUser().first()?.id ?: throw NullPointerException()
        return repository.isVehicle(uuid)
    }
}