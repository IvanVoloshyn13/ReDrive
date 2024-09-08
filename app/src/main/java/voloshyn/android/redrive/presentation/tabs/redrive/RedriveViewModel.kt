package voloshyn.android.redrive.presentation.tabs.redrive

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.tabs.profile.UserTuple
import voloshyn.android.domain.useCase.auth.IsSignedInUseCase
import javax.inject.Inject

@HiltViewModel
class RedriveViewModel @Inject constructor(
    isSignedInUseCase: IsSignedInUseCase,
) : ViewModel() {
    init {
        currentUser(isSignedInUseCase)
    }

    private fun currentUser(isSignedInUseCase: IsSignedInUseCase) {
        val currentUser = UserTuple.EMPTY_USER
        val result = isSignedInUseCase.invoke()
        when (result) {
            is AppResult.Success -> {
                result.data?.let {
                    currentUser.copy(
                        fullName = it.fullName,
                        email = it.email
                    )
                }
            }

            is AppResult.Error -> {}
        }
    }
}