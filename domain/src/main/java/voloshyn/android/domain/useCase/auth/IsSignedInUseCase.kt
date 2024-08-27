package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.models.UserTuple
import voloshyn.android.domain.repository.InitRepository

class IsSignedInUseCase(private val repository: InitRepository) {
     fun invoke(): AppResult<UserTuple?, AuthError.Auth> {
        return repository.isSignedIn()
    }
}