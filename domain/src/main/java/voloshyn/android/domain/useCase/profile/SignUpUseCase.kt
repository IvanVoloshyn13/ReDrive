package voloshyn.android.domain.useCase.profile

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.models.User
import voloshyn.android.domain.models.UserTuple
import voloshyn.android.domain.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend fun invoke(user: User): AppResult<UserTuple, AuthError> {
        return repository.signUp(user)
    }
}