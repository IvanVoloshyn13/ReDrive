package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.models.tabs.profile.UserTuple
import voloshyn.android.domain.repository.AuthRepository

class IsSignedInUseCase(private val repository: AuthRepository) {
     fun invoke(): AppResult<UserTuple?, AuthError.FirebaseAuth> {
        return repository.isSignedIn()
    }
}