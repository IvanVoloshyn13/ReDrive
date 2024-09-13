package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.models.tabs.profile.User
import voloshyn.android.domain.models.tabs.profile.UserTuple
import voloshyn.android.domain.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend fun invoke(user: User): AppResult<UserTuple, AuthError.FirebaseAuth> {
        return repository.signUp(user)
    }
}