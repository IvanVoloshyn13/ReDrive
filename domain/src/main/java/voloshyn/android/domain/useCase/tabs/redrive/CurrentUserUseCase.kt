package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.profile.UserTuple
import voloshyn.android.domain.repository.tabs.RedriveRepository

class CurrentUserUseCase(private val repository: RedriveRepository) {
    suspend fun invoke(email:String): AppResult<UserTuple,DataError.Locale> {
        val user = repository.currentUser(email)
        return user
    }
}